package com.ede.standyourground.game.model;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class FootSoldier extends MovableUnit {

    private final Circle circle;

    public FootSoldier(List<LatLng> waypoints, LatLng position, Path path, boolean isEnemy, Circle circle) {
        super(50, waypoints, position, path, isEnemy);
        this.circle = circle;
        this.visionRadius = .1;
    }

    @Override
    public void onRender() {
        circle.setCenter(getCurrentPosition());
        circle.setVisible(isVisible());
    }
}
