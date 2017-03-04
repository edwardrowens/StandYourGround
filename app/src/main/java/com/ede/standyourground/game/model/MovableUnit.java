package com.ede.standyourground.game.model;

import com.ede.standyourground.game.model.api.Attacker;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;


public abstract class MovableUnit extends Unit implements Attacker {

    private int currentTarget;

    private final Path path;
    private final AtomicLong lastUpdated;
    private final AtomicReference<Double> distanceTraveled;
    private final boolean reachedEnemyBase;
    private final List<LatLng> waypoints;
    private final AtomicReference<Double> mph = new AtomicReference<>(startingMph());

    protected abstract double startingMph();

    public MovableUnit(List<LatLng> waypoints, LatLng position, Path path, double radius, boolean isEnemy) {
        super(position, radius, isEnemy);
        this.path = path;
        currentTarget = 0;
        this.lastUpdated = new AtomicLong(getCreatedTime());
        this.distanceTraveled = new AtomicReference<>(0d);
        this.reachedEnemyBase = false;
        this.waypoints = waypoints;
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

    public long getLastUpdated() {
        return lastUpdated.get();
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated.set(lastUpdated);
    }

    public void setDistanceTraveled(double distanceTraveled) {
        this.distanceTraveled.set(distanceTraveled);
    }

    public double getDistanceTraveled() {
        return distanceTraveled.get();
    }

    public List<LatLng> getWaypoints() {
        return waypoints;
    }

    public double getMph() {
        return mph.get();
    }

    public void setMph(double mph) {
        this.mph.set(mph);
    }
}
