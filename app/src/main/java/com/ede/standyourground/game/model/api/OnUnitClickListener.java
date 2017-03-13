package com.ede.standyourground.game.model.api;

import android.graphics.Point;

import com.ede.standyourground.app.activity.MapsActivity;
import com.ede.standyourground.app.ui.UnitGroupComponent;
import com.ede.standyourground.framework.Logger;
import com.ede.standyourground.framework.api.LatLngService;
import com.ede.standyourground.framework.api.MathService;
import com.ede.standyourground.framework.dagger.providers.GoogleMapProvider;
import com.ede.standyourground.game.framework.management.impl.WorldManager;
import com.ede.standyourground.game.model.Unit;
import com.ede.standyourground.game.model.Units;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.Circle;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Lazy;

/**
 *
 */
@Singleton
public class OnUnitClickListener implements GoogleMap.OnCircleClickListener {

    private static final Logger logger = new Logger(OnUnitClickListener.class);

    // In meters
    private static final double EQUAL_DISTANCE_TOLERANCE = 5d;

    private final Lazy<GoogleMapProvider> googleMapProvider;
    private final Lazy<MathService> mathService;
    private final Lazy<WorldManager> worldManager;
    private final Lazy<LatLngService> latLngService;

    @Inject
    OnUnitClickListener(Lazy<GoogleMapProvider> googleMapProvider,
                        Lazy<MathService> mathService,
                        Lazy<WorldManager> worldManager,
                        Lazy<LatLngService> latLngService) {
        this.googleMapProvider = googleMapProvider;
        this.mathService = mathService;
        this.worldManager = worldManager;
        this.latLngService = latLngService;
    }

    @Override
    public void onCircleClick(Circle circle) {
        UnitGroupComponent unitGroupComponent = (UnitGroupComponent) MapsActivity.getComponent(UnitGroupComponent.class);
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
        for (Map.Entry<UUID, Circle> entry : MapsActivity.getCircles().entrySet()) {
            Unit unit = worldManager.get().getUnit(entry.getKey());
            boolean samePosition = latLngService.get().withinDistance(unit.getCurrentPosition(), circle.getCenter(), EQUAL_DISTANCE_TOLERANCE);
            if (!unit.isEnemy() && samePosition) {
                unitsOnPosition.add(unit);
                bag.put(unit.getType(), bag.containsKey(unit.getType()) ? bag.get(unit.getType()) + 1 : 1);
                logger.d("%s is on this circle's position. %d in the bag", unit.getType().toString(), bag.get(unit.getType()));
            }
        }

        if (unitsOnPosition.size() > 1) {
            for (Map.Entry<Units, Integer> e : bag.entrySet()) {
                if (e.getValue() == 1) {
                    for (Unit u : unitsOnPosition) {
                        if (u.getType().equals(e.getKey())) {
                            logger.d("adding %s with health %.5f", u.getType().toString(), u.getHealth() / (float)u.getMaxHealth());
                            unitGroupComponent.createUnitGroupBlockHealthBar(u.getId(), u.getType(), u.getHealth() / (float)u.getMaxHealth());
                        }
                    }
                } else {
                    logger.d("adding %s with count %d", e.getKey().toString(), e.getValue());
                    List<UUID> unitIds = new ArrayList<>();
                    for (Unit u : unitsOnPosition) {
                        unitIds.add(u.getId());
                    }
                    unitGroupComponent.createUnitGroupBlockCount(unitIds, e.getKey(), e.getValue());
                }
            }
        }
    }
}
