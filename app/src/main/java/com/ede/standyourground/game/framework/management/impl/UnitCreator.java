package com.ede.standyourground.game.framework.management.impl;


import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;

import com.ede.standyourground.framework.providers.GoogleMapProvider;
import com.ede.standyourground.game.model.FootSoldier;
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
    private final GoogleMapProvider googleMapProvider;

    @Inject
    UnitCreator(GoogleMapProvider googleMapProvider,
                Lazy<WorldManager> worldManager) {
        this.googleMapProvider = googleMapProvider;
        this.worldManager = worldManager;
    }

    public void createPlayerUnit(final List<LatLng> route, final LatLng position, Units units) {
        switch (units) {
            case FOOT_SOLDIER:
                mainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Circle circle = googleMapProvider.getGoogleMap().addCircle(new CircleOptions().center(position).clickable(false).radius(50).fillColor(Color.BLUE).strokeColor(Color.BLACK).zIndex(1.0f));
                        Unit unit = new FootSoldier(route, position, false, circle);
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
                        Circle circle = googleMapProvider.getGoogleMap().addCircle(new CircleOptions().center(position).clickable(false).radius(50).fillColor(Color.RED).strokeColor(Color.BLACK).zIndex(1.0f));
                        Unit unit = new FootSoldier(route, position, true, circle);
                        worldManager.get().addEnemyUnit(unit);
                    }
                });
        }
    }
}
