package com.ede.standyourground.game.framework.management.impl;


import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;

import com.ede.standyourground.game.model.FootSoldier;
import com.ede.standyourground.game.model.Unit;
import com.ede.standyourground.game.model.Units;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.UUID;

public class UnitCreator {

    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    private final GoogleMap googleMap;

    public UnitCreator(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    public void createUnit(final List<LatLng> route, final LatLng position, Units units) {
        switch (units) {
            case FOOT_SOLDIER:
                mainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Circle circle = googleMap.addCircle(new CircleOptions().center(position).clickable(false).radius(50).fillColor(Color.BLUE).strokeColor(Color.BLUE).zIndex(1.0f));
                        Unit unit = new FootSoldier(route, position, circle);
                        WorldManager.getInstance().addUnit(unit);
                    }
                });
        }
    }

    public void createUnit(final List<LatLng> route, final LatLng position, Units units, final UUID gameSessionId) {
        switch (units) {
            case FOOT_SOLDIER:
                mainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Circle circle = googleMap.addCircle(new CircleOptions().center(position).clickable(false).radius(50).fillColor(Color.BLUE).strokeColor(Color.BLUE).zIndex(1.0f));
                        Unit unit = new FootSoldier(route, position, circle);
                        WorldManager.getInstance().addUnit(unit, gameSessionId);
                    }
                });
        }
    }
}
