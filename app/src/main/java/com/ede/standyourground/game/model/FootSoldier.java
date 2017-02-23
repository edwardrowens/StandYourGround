package com.ede.standyourground.game.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class FootSoldier extends MovableUnit {
    public FootSoldier(List<LatLng> waypoints, LatLng position) {
        super(50, waypoints, position);
    }
}
