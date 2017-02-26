package com.ede.standyourground.game.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;


public abstract class MovableUnit extends Unit {

    private double mph;
    private int currentTarget;

    public MovableUnit(double mph, List<LatLng> waypoints, LatLng position, boolean isEnemy) {
        super(position, waypoints, isEnemy);
        this.mph = mph;
        currentTarget = 0;
    }

    public double getMph() {
        return mph;
    }

    public void incrementTarget() {
        if (currentTarget < getWaypoints().size() - 1)
            ++currentTarget;
    }

    public int getCurrentTarget() {
        return currentTarget;
    }
}
