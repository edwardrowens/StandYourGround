package com.ede.standyourground.game.impl.service;


import com.ede.standyourground.framework.api.service.RouteService;
import com.ede.standyourground.game.api.model.Cell;
import com.ede.standyourground.game.api.model.Hostility;
import com.ede.standyourground.game.api.model.Path;
import com.ede.standyourground.game.api.model.Unit;
import com.ede.standyourground.game.api.model.UnitType;
import com.ede.standyourground.game.api.service.WorldGridService;
import com.ede.standyourground.game.impl.model.BankNeutralCamp;
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
    private final Lazy<WorldGridService> worldGridService;

    @Inject
    UnitFactory(Lazy<RouteService> routeService,
                Lazy<WorldGridService> worldGridService) {
        this.routeService = routeService;
        this.worldGridService = worldGridService;
    }

    public Unit createPlayerUnit(final List<LatLng> route, final LatLng position, UnitType unitType) {
        return createUnit(unitType, position, route, Hostility.FRIENDLY);
    }

    public Unit createEnemyUnit(final List<LatLng> route, final LatLng position, UnitType unitType) {
        return createUnit(unitType, position, route, Hostility.ENEMY);
    }

    public Unit createNeutralUnit(final LatLng position, UnitType unitType, String name, String photoReference, Hostility hostility) {
        return createNeutralUnit(unitType, position, name, photoReference, hostility);
    }

    private Unit createUnit(UnitType type, LatLng position, List<LatLng> route, Hostility hostility) {
        Unit unit;
        Cell cell = worldGridService.get().calculateCellPosition(position);
        switch (type) {
            case FOOT_SOLDIER:
                Path path = new Path(route, routeService.get().getDistanceOfSteps(route, position));
                unit = new FootSoldier(route, position, path, hostility, cell);
                break;
            case BASE:
                unit = new Base(position, hostility, cell);
                break;
            case MARAUDER:
                Path pathM = new Path(route, routeService.get().getDistanceOfSteps(route, position));
                unit = new Marauder(route, position, pathM, hostility, cell);
                break;
            case MEDIC:
                Path medicPath = new Path(route, routeService.get().getDistanceOfSteps(route, position));
                unit = new Medic(route, position, medicPath, hostility, cell);
                break;
            default:
                throw new IllegalArgumentException("UnitType " + type.toString() + " is not currently supported.");
        }
        worldGridService.get().addUnitAtCell(cell, unit);
        return unit;
    }

    private Unit createNeutralUnit(UnitType type, LatLng position, String name, String photoReference, Hostility hostility) {
        Unit unit;
        Cell cell = worldGridService.get().calculateCellPosition(position);
        switch(type) {
            case MEDIC_NEUTRAL_CAMP:
                unit = new MedicNeutralCamp(position, name, photoReference, hostility, cell);
                break;
            case BANK_NEUTRAL_CAMP:
                unit = new BankNeutralCamp(position, name, photoReference, hostility, cell);
                break;
            default:
                throw new IllegalArgumentException("UnitType " + type.toString() + " is not currently supported.");
        }
        worldGridService.get().addUnitAtCell(cell, unit);
        return unit;
    }
}
