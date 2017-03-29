package com.ede.standyourground.game.impl.service;


import com.ede.standyourground.framework.api.service.RouteService;
import com.ede.standyourground.game.api.model.Hostility;
import com.ede.standyourground.game.api.model.Path;
import com.ede.standyourground.game.api.model.Unit;
import com.ede.standyourground.game.api.model.Units;
import com.ede.standyourground.game.impl.model.Base;
import com.ede.standyourground.game.impl.model.FootSoldier;
import com.ede.standyourground.game.impl.model.Marauder;
import com.ede.standyourground.game.impl.model.Medic;
import com.ede.standyourground.game.impl.model.MedicNeutralCamp;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;

public class UnitFactory {

    private final Lazy<RouteService> routeService;

    @Inject
    UnitFactory(Lazy<RouteService> routeService) {
        this.routeService = routeService;
    }

    public Unit createPlayerUnit(final List<LatLng> route, final LatLng position, Units units) {
        return createUnit(units, position, route, Hostility.FRIENDLY);
    }

    public Unit createEnemyUnit(final List<LatLng> route, final LatLng position, Units units) {
        return createUnit(units, position, route, Hostility.ENEMY);
    }

    public Unit createNeutralUnit(final LatLng position, Units units, String name, String photoReference, Hostility hostility) {
        return createNeutralUnit(units, position, name, photoReference, hostility);
    }

    private Unit createUnit(Units type, LatLng position, List<LatLng> route, Hostility hostility) {
        Unit unit;
        switch (type) {
            case FOOT_SOLDIER:
                Path path = new Path(route, routeService.get().getDistanceOfSteps(route, position));
                unit = new FootSoldier(route, position, path, hostility);
                break;
            case BASE:
                unit = new Base(position, hostility);
                break;
            case MARAUDER:
                Path pathM = new Path(route, routeService.get().getDistanceOfSteps(route, position));
                unit = new Marauder(route, position, pathM, hostility);
                break;
            case MEDIC:
                Path medicPath = new Path(route, routeService.get().getDistanceOfSteps(route, position));
                unit = new Medic(route, position, medicPath, hostility);
                break;
            default:
                throw new IllegalArgumentException("Units " + type.toString() + " is not currently supported.");
        }

        return unit;
    }

    private Unit createNeutralUnit(Units type, LatLng position, String name, String photoReference, Hostility hostility) {
        Unit unit;
        switch(type) {
            case MEDIC_NEUTRAL_CAMP:
                unit = new MedicNeutralCamp(position, name, photoReference, hostility);
                break;
            default:
                throw new IllegalArgumentException("Units " + type.toString() + " is not currently supported.");
        }
        return unit;
    }
}
