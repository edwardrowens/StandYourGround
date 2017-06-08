package com.ede.standyourground.app.event;

import android.graphics.Point;

import com.ede.standyourground.app.ui.api.component.UnitGroupBlockCountComponentFactory;
import com.ede.standyourground.app.ui.api.component.UnitGroupBlockHealthBarComponentFactory;
import com.ede.standyourground.app.ui.api.service.UnitChoicesMenuService;
import com.ede.standyourground.app.ui.api.service.UnitGroupComponentService;
import com.ede.standyourground.app.ui.impl.component.NeutralCampListingComponent;
import com.ede.standyourground.app.ui.impl.component.UnitChoicesMenuComponent;
import com.ede.standyourground.app.ui.impl.component.UnitGroupBlockCount;
import com.ede.standyourground.app.ui.impl.component.UnitGroupBlockHealthBarComponent;
import com.ede.standyourground.app.ui.impl.component.UnitGroupComponent;
import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.framework.api.dagger.providers.GoogleMapProvider;
import com.ede.standyourground.framework.api.service.LatLngService;
import com.ede.standyourground.framework.api.service.MathService;
import com.ede.standyourground.game.api.model.Hostility;
import com.ede.standyourground.game.api.model.MovableUnit;
import com.ede.standyourground.game.api.model.NeutralCamp;
import com.ede.standyourground.game.api.model.Unit;
import com.ede.standyourground.game.api.model.UnitType;
import com.ede.standyourground.game.api.service.UnitService;
import com.ede.standyourground.game.api.service.WorldGridService;
import com.ede.standyourground.game.impl.model.Base;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;

import dagger.Lazy;

/**
 *
 */
public class OnMarkerClickListenerFactory {

    private static final Logger logger = new Logger(OnMarkerClickListenerFactory.class);

    // In meters
    private static final double EQUAL_DISTANCE_TOLERANCE = 50d;

    private final Lazy<GoogleMapProvider> googleMapProvider;
    private final Lazy<MathService> mathService;
    private final Lazy<UnitService> unitService;
    private final Lazy<LatLngService> latLngService;
    private final Lazy<UnitChoicesMenuService> unitChoicesMenuService;
    private final Lazy<WorldGridService> worldGridService;
    private final Lazy<UnitGroupComponentService> unitGroupComponentService;
    private final Lazy<UnitGroupBlockHealthBarComponentFactory> unitGroupBlockHealthBarComponentFactory;
    private final Lazy<UnitGroupBlockCountComponentFactory> unitGroupBlockCountComponentFactory;

    @Inject
    OnMarkerClickListenerFactory(Lazy<GoogleMapProvider> googleMapProvider,
                                 Lazy<MathService> mathService,
                                 Lazy<UnitService> unitService,
                                 Lazy<LatLngService> latLngService,
                                 Lazy<UnitChoicesMenuService> unitChoicesMenuService,
                                 Lazy<WorldGridService> worldGridService,
                                 Lazy<UnitGroupComponentService> unitGroupComponentService,
                                 Lazy<UnitGroupBlockHealthBarComponentFactory> unitGroupBlockHealthBarComponentFactory,
                                 Lazy<UnitGroupBlockCountComponentFactory> unitGroupBlockCountComponentFactory) {
        this.googleMapProvider = googleMapProvider;
        this.mathService = mathService;
        this.unitService = unitService;
        this.latLngService = latLngService;
        this.unitChoicesMenuService = unitChoicesMenuService;
        this.worldGridService = worldGridService;
        this.unitGroupComponentService = unitGroupComponentService;
        this.unitGroupBlockHealthBarComponentFactory = unitGroupBlockHealthBarComponentFactory;
        this.unitGroupBlockCountComponentFactory = unitGroupBlockCountComponentFactory;
    }

    public GoogleMap.OnMarkerClickListener createOnMarkerClickedListener(final UnitGroupComponent unitGroupComponent, final NeutralCampListingComponent neutralCampListingComponent, final UnitChoicesMenuComponent unitChoicesMenuComponent) {
        return new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.getTag() == null) {
                    logger.i("A marker was clicked with a null tag.");
                    return true;
                }
                unitGroupComponentService.get().clear(unitGroupComponent);
                neutralCampListingComponent.clear();

                Unit unitClicked = unitService.get().getUnit((UUID) marker.getTag());
                if (unitClicked == null) {
                    return true;
                }
                LatLng clickedUnitPosition = unitClicked instanceof MovableUnit ? ((MovableUnit) unitClicked).getCurrentPosition() : unitClicked.getStartingPosition();

                final Map<UnitType, Integer> bag = new HashMap<>();
                final List<Unit> unitsOnPosition = new ArrayList<>();

                Projection projection = googleMapProvider.get().getGoogleMap().getProjection();
                Point center = projection.toScreenLocation(clickedUnitPosition);
                Point edge = projection.toScreenLocation(SphericalUtil.computeOffset(clickedUnitPosition, unitClicked.getRadius(), 0d));
                double lineDistance = mathService.get().calculateLinearDistance(center, edge);

                if (unitClicked instanceof NeutralCamp) {
                    neutralCampListingComponent.setTextAndPhoto(((NeutralCamp) unitClicked).getName(), ((NeutralCamp) unitClicked).getPhotoReference(), center, lineDistance);
                } else if (unitClicked instanceof Base && unitClicked.getHostility() == Hostility.FRIENDLY) {
                    unitChoicesMenuService.get().realign(unitChoicesMenuComponent);
                } else {
                    for (Unit unit : worldGridService.get().retrieveUnitsAtCell(unitClicked.getCell())) {
                        LatLng unitPosition = unit instanceof MovableUnit ? ((MovableUnit) unit).getCurrentPosition() : unit.getStartingPosition();
                        boolean samePosition = latLngService.get().withinDistance(unitPosition, clickedUnitPosition, EQUAL_DISTANCE_TOLERANCE);
                        if (samePosition) {
                            unitsOnPosition.add(unit);
                            bag.put(unit.getType(), bag.containsKey(unit.getType()) ? bag.get(unit.getType()) + 1 : 1);
                        }
                    }

                    for (Map.Entry<UnitType, Integer> e : bag.entrySet()) {
                        if (e.getValue() == 1) {
                            for (Unit u : unitsOnPosition) {
                                if (u.getType().equals(e.getKey())) {
                                    UnitGroupBlockHealthBarComponent unitGroupBlockHealthBarComponent = unitGroupBlockHealthBarComponentFactory.get().createUnitGroupBlockHealthBar(unitGroupComponent.getActivity(),
                                            unitGroupComponent.getContainer(),
                                            u.getId(),
                                            u.getHealth() / (float) u.getMaxHealth(),
                                            u.getType());

                                    unitGroupComponentService.get().addUnitGroupBlock(unitGroupComponent, unitGroupBlockHealthBarComponent);
                                }
                            }
                        } else {
                            Set<UUID> unitIds = new HashSet<>();
                            for (Unit u : unitsOnPosition) {
                                if (u.getType().equals(e.getKey())) {
                                    unitIds.add(u.getId());
                                }
                            }

                            UnitGroupBlockCount unitGroupBlockCount = unitGroupBlockCountComponentFactory.get().createUnitGroupBlockCountComponent(unitGroupComponent.getActivity(),
                                    unitIds,
                                    e.getKey());

                            unitGroupComponentService.get().addUnitGroupBlock(unitGroupComponent, unitGroupBlockCount);
                        }
                    }
                    unitGroupComponentService.get().realign(unitGroupComponent, marker.getPosition());
                }
                return true;
            }
        };
    }
}
