package com.ede.standyourground.game.framework.management.impl;


import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;

import com.ede.standyourground.app.activity.MapsActivity;
import com.ede.standyourground.framework.api.MathService;
import com.ede.standyourground.framework.api.RouteService;
import com.ede.standyourground.framework.dagger.providers.GoogleMapProvider;
import com.ede.standyourground.game.model.Base;
import com.ede.standyourground.game.model.FootSoldier;
import com.ede.standyourground.game.model.Marauder;
import com.ede.standyourground.game.model.Path;
import com.ede.standyourground.game.model.Unit;
import com.ede.standyourground.game.model.Units;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;

public class UnitCreator {

    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    private final Lazy<WorldManager> worldManager;
    private final Lazy<GoogleMapProvider> googleMapProvider;
    private final Lazy<RouteService> routeService;
    private final Lazy<MathService> mathService;

    @Inject
    UnitCreator(Lazy<GoogleMapProvider> googleMapProvider,
                Lazy<WorldManager> worldManager,
                Lazy<RouteService> routeService,
                Lazy<MathService> mathService) {
        this.googleMapProvider = googleMapProvider;
        this.worldManager = worldManager;
        this.routeService = routeService;
        this.mathService = mathService;
    }

    public void createPlayerUnit(final List<LatLng> route, final LatLng position, Units units) {
        switch (units) {
            case FOOT_SOLDIER:
                mainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        createUnit(Units.FOOT_SOLDIER, Color.MAGENTA, position, route, false);
                    }
                });
                break;
            case BASE:
                mainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        createUnit(Units.BASE, Color.BLUE, position, route, false);
                    }
                });
                break;
            case MARAUDER:
                mainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        createUnit(Units.MARAUDER, Color.CYAN, position, route, false);
                    }
                });
        }
    }

    public void createEnemyUnit(final List<LatLng> route, final LatLng position, Units units) {
        switch (units) {
            case FOOT_SOLDIER:
                mainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        createUnit(Units.FOOT_SOLDIER, Color.RED, position, route, true);
                    }
                });
                break;
            case BASE:
                mainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        createUnit(Units.BASE, Color.RED, position, route, true);
                    }
                });
                break;
            case MARAUDER:
                mainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        createUnit(Units.MARAUDER, Color.RED, position, route, true);
                    }
                });
        }
    }

    private void createUnit(Units type, int color, LatLng position, List<LatLng> route, boolean isEnemy) {
        switch (type) {
            case FOOT_SOLDIER:
                Path path = new Path(route, routeService.get().getDistanceOfSteps(route, position));
                CircleOptions circleOptions = Units.FOOT_SOLDIER.getCircleOptions().center(position).fillColor(color);
                Unit unit = new FootSoldier(route, position, path, Units.FOOT_SOLDIER.getCircleOptions().getRadius(), isEnemy);
                MapsActivity.addCircle(unit.getId(), circleOptions);
                worldManager.get().addEnemyUnit(unit);
                break;
            case BASE:
                CircleOptions circleOptionsBase = Units.BASE.getCircleOptions().center(position).fillColor(color);
                Unit base = new Base(position, isEnemy);
                MapsActivity.addCircle(base.getId(), circleOptionsBase);
                worldManager.get().addEnemyUnit(base);
                break;

            case MARAUDER:
                Path pathM = new Path(route, routeService.get().getDistanceOfSteps(route, position));
                CircleOptions circleOptionsM = Units.MARAUDER.getCircleOptions().center(position).fillColor(color);
                Unit unitM = new Marauder(route, position, pathM, Units.MARAUDER.getCircleOptions().getRadius(), isEnemy);
                MapsActivity.addCircle(unitM.getId(), circleOptionsM);
                worldManager.get().addEnemyUnit(unitM);
                break;
        }
    }
}
