package com.ede.standyourground.game.impl.service;


import com.ede.standyourground.R;
import com.ede.standyourground.framework.api.service.RouteService;
import com.ede.standyourground.game.api.model.Path;
import com.ede.standyourground.game.api.model.Unit;
import com.ede.standyourground.game.api.model.Units;
import com.ede.standyourground.game.impl.model.Base;
import com.ede.standyourground.game.impl.model.FootSoldier;
import com.ede.standyourground.game.impl.model.Marauder;
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
        Unit unit;
        switch (units) {
            case FOOT_SOLDIER:
                unit = createUnit(Units.FOOT_SOLDIER, R.color.magenta, position, route, false);
                break;
            case BASE:
                unit = createUnit(Units.BASE, R.color.blue, position, route, false);
                break;
            case MARAUDER:
                unit = createUnit(Units.MARAUDER, R.color.cyan, position, route, false);
                break;
            default:
                throw new IllegalArgumentException("Units " + units.toString() + " is not currently supported.");
        }

        return unit;
    }

    public Unit createEnemyUnit(final List<LatLng> route, final LatLng position, Units units) {
        Unit unit;
        switch (units) {
            case FOOT_SOLDIER:
                unit = createUnit(Units.FOOT_SOLDIER, R.color.red, position, route, true);
                break;
            case BASE:
                unit = createUnit(Units.BASE, R.color.red, position, route, true);
                break;
            case MARAUDER:
                unit = createUnit(Units.MARAUDER, R.color.red, position, route, true);
                break;
            default:
                throw new IllegalArgumentException("Units " + units.toString() + " is not currently supported.");
        }

        return unit;
    }

    private Unit createUnit(Units type, int color, LatLng position, List<LatLng> route, boolean isEnemy) {
//        // hacky way in order to get the proper conversion of colors
//        Activity activity = MapsActivity.getComponent(HealthBarComponent.class).getActivity();
//        color = activity.getResources().getColor(color);
        Unit unit;
        switch (type) {
            case FOOT_SOLDIER:
                Path path = new Path(route, routeService.get().getDistanceOfSteps(route, position));
//                CircleOptions circleOptions = Units.FOOT_SOLDIER.getCircleOptions().center(position).fillColor(color);
                unit = new FootSoldier(route, position, path, Units.FOOT_SOLDIER.getCircleOptions().getRadius(), isEnemy);
//                MapsActivity.addCircle(unit.getId(), circleOptions);
//                unitService.get().addEnemyUnit(unit);
                break;
            case BASE:
//                CircleOptions circleOptionsBase = Units.BASE.getCircleOptions().center(position).fillColor(color);
                unit = new Base(position, isEnemy);
//                MapsActivity.addCircle(base.getId(), circleOptionsBase);
//                unitService.get().addEnemyUnit(base);
                break;

            case MARAUDER:
                Path pathM = new Path(route, routeService.get().getDistanceOfSteps(route, position));
//                CircleOptions circleOptionsM = Units.MARAUDER.getCircleOptions().center(position).fillColor(color);
                unit = new Marauder(route, position, pathM, Units.MARAUDER.getCircleOptions().getRadius(), isEnemy);
//                MapsActivity.addCircle(unitM.getId(), circleOptionsM);
//                unitService.get().addEnemyUnit(unitM);
                break;
            default:
                throw new IllegalArgumentException("Units " + type.toString() + " is not currently supported.");
        }

        return unit;
    }
}
