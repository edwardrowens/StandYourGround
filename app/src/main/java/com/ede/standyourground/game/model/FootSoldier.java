package com.ede.standyourground.game.model;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class FootSoldier extends MovableUnit {

    private Circle circle;

    public FootSoldier(List<LatLng> waypoints, LatLng position, boolean isEnemy, Circle circle) {
        super(50, waypoints, position, isEnemy);
        this.circle = circle;
        this.visionRadius = 1;
    }

    @Override
    public void render() {
        circle.setCenter(getPosition());
    }
}
