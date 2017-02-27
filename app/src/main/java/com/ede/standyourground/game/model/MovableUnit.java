package com.ede.standyourground.game.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


public abstract class MovableUnit extends Unit {

    private final AtomicReference<Double> mph;
    private int currentTarget;
    private final Path path;

    public MovableUnit(double mph, List<LatLng> waypoints, LatLng position, Path path, boolean isEnemy) {
        super(position, waypoints, isEnemy);
        this.path = path;
        this.mph = new AtomicReference<>(mph);
        currentTarget = 0;
    }

    public double getMph() {
        return mph.get();
    }

    public void setMph(double mph) {
        this.mph.set(mph);
    }

    public void incrementTarget() {
        if (currentTarget < getWaypoints().size() - 1)
            ++currentTarget;
    }

    public int getCurrentTarget() {
        return currentTarget;
    }

    public Path getPath() {
        return path;
    }
}
