package com.ede.standyourground.game.framework.management.impl;


import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;

import com.ede.standyourground.framework.api.RouteService;
import com.ede.standyourground.framework.dagger.providers.GoogleMapProvider;
import com.ede.standyourground.game.model.Base;
import com.ede.standyourground.game.model.FootSoldier;
import com.ede.standyourground.game.model.Path;
import com.ede.standyourground.game.model.Unit;
import com.ede.standyourground.game.model.Units;
import com.google.android.gms.maps.model.Circle;
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

    @Inject
    UnitCreator(Lazy<GoogleMapProvider> googleMapProvider,
                Lazy<WorldManager> worldManager,
                Lazy<RouteService> routeService) {
        this.googleMapProvider = googleMapProvider;
        this.worldManager = worldManager;
        this.routeService = routeService;
    }

    public void createPlayerUnit(final List<LatLng> route, final LatLng position, Units units) {
        switch (units) {
            case FOOT_SOLDIER:
                mainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Path path = new Path(route, routeService.get().getDistanceOfSteps(route, position));
                        Circle circle = googleMapProvider.get().getGoogleMap().addCircle(new CircleOptions().center(position)
                                                                                                            .clickable(false)
                                                                                                            .radius(50)
                                                                                                            .fillColor(Color.BLUE)
                                                                                                            .strokeColor(Color.BLACK)
                                                                                                            .zIndex(1.0f)
                                                                                                            .strokeWidth(5));
                        Unit unit = new FootSoldier(route, position, path, false, circle);
                        worldManager.get().addPlayerUnit(unit);
                    }
                });
                break;
            case BASE:
                mainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Unit unit = new Base(position, false);
                        worldManager.get().addPlayerUnit(unit);
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
                        Path path = new Path(route, routeService.get().getDistanceOfSteps(route, position));
                        Circle circle = googleMapProvider.get().getGoogleMap().addCircle(new CircleOptions().center(position)
                                                                                                            .clickable(false)
                                                                                                            .radius(50)
                                                                                                            .fillColor(Color.RED)
                                                                                                            .strokeColor(Color.BLACK)
                                                                                                            .zIndex(1.0f)
                                                                                                            .strokeWidth(5));
                        Unit unit = new FootSoldier(route, position, path, true, circle);
                        worldManager.get().addEnemyUnit(unit);
                    }
                });
                break;
            case BASE:
                mainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Unit unit = new Base(position, true);
                        worldManager.get().addEnemyUnit(unit);
                    }
                });
        }
    }
}
