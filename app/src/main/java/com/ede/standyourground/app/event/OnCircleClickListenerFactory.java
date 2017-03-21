package com.ede.standyourground.app.event;

import android.graphics.Point;

import com.ede.standyourground.app.ui.impl.component.UnitGroupComponent;
import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.framework.api.dagger.providers.GoogleMapProvider;
import com.ede.standyourground.framework.api.service.LatLngService;
import com.ede.standyourground.framework.api.service.MathService;
import com.ede.standyourground.game.api.model.MovableUnit;
import com.ede.standyourground.game.api.model.Unit;
import com.ede.standyourground.game.api.model.Units;
import com.ede.standyourground.game.api.service.UnitService;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import dagger.Lazy;

/**
 *
 */
public class OnCircleClickListenerFactory {

    private static final Logger logger = new Logger(OnCircleClickListenerFactory.class);

    // In meters
    private static final double EQUAL_DISTANCE_TOLERANCE = 5d;

    private final Lazy<GoogleMapProvider> googleMapProvider;
    private final Lazy<MathService> mathService;
    private final Lazy<UnitService> unitService;
    private final Lazy<LatLngService> latLngService;

    @Inject
    OnCircleClickListenerFactory(Lazy<GoogleMapProvider> googleMapProvider,
                                 Lazy<MathService> mathService,
                                 Lazy<UnitService> unitService,
                                 Lazy<LatLngService> latLngService) {
        this.googleMapProvider = googleMapProvider;
        this.mathService = mathService;
        this.unitService = unitService;
        this.latLngService = latLngService;
    }

    public GoogleMap.OnCircleClickListener createOnCircleClickedListener(final UnitGroupComponent unitGroupComponent) {
        return new GoogleMap.OnCircleClickListener() {
            @Override
            public void onCircleClick(Circle circle) {
                unitGroupComponent.clear();
                Projection projection = googleMapProvider.get().getGoogleMap().getProjection();
                Point center = projection.toScreenLocation(circle.getCenter());
                Point edge = projection.toScreenLocation(SphericalUtil.computeOffset(circle.getCenter(), circle.getRadius(), 0d));
                double lineDistance = mathService.get().calculateLinearDistance(center, edge);

                // The 5 is for padding
                final Point point = new Point();
                point.x = center.x + (int) Math.round(lineDistance) + 5;
                point.y = center.y + (int) Math.round(lineDistance) + 5;

                unitGroupComponent.setPoint(point);

                Map<Units, Integer> bag = new HashMap<>();
                List<Unit> unitsOnPosition = new ArrayList<>();
                for (Unit unit : unitService.get().getUnits()) {
                    LatLng unitPosition = unit instanceof MovableUnit ? ((MovableUnit) unit).getCurrentPosition() : unit.getStartingPosition();
                    boolean samePosition = latLngService.get().withinDistance(unitPosition, circle.getCenter(), EQUAL_DISTANCE_TOLERANCE);
                    if (!unit.isEnemy() && samePosition) {
                        unitsOnPosition.add(unit);
                        bag.put(unit.getType(), bag.containsKey(unit.getType()) ? bag.get(unit.getType()) + 1 : 1);
                    }
                }

                if (unitsOnPosition.size() > 1) {
                    for (Map.Entry<Units, Integer> e : bag.entrySet()) {
                        if (e.getValue() == 1) {
                            for (Unit u : unitsOnPosition) {
                                if (u.getType().equals(e.getKey())) {
                                    unitGroupComponent.createAndAddUnitGroupBlockHealthBar(u.getId(), u.getType(), u.getHealth() / (float) u.getMaxHealth());
                                }
                            }
                        } else {
                            List<UUID> unitIds = new ArrayList<>();
                            for (Unit u : unitsOnPosition) {
                                if (u.getType().equals(e.getKey())) {
                                    unitIds.add(u.getId());
                                }
                            }
                            unitGroupComponent.createUnitGroupBlockCount(unitIds, e.getKey(), e.getValue());
                        }
                    }
                }
            }
        };
    }
}
