package com.ede.standyourground.game.model;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class FootSoldier extends MovableUnit {

    private Circle circle;

    public FootSoldier(List<LatLng> waypoints, LatLng position, Circle circle) {
        super(50, waypoints, position);
        this.circle = circle;
    }

    @Override
    public void render() {
        circle.setCenter(getPosition());
    }
}
