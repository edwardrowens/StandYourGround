package com.ede.standyourground.game.api.model;

import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.game.api.event.listener.PositionChangeListener;
import com.ede.standyourground.game.api.event.observer.PositionChangeObserver;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;


public abstract class MovableUnit extends Unit implements Attacker, PositionChangeObserver {

    private static final Logger logger = new Logger(MovableUnit.class);

    private int currentTarget;

    private final AtomicReference<LatLng> currentPosition;
    private final Path path;
    private final AtomicLong lastUpdated;
    private final AtomicReference<Double> distanceTraveled;
    private final List<LatLng> waypoints;
    private final AtomicReference<Double> mph = new AtomicReference<>(startingMph());

    private PositionChangeListener positionChangeListener;

    protected abstract double startingMph();

    public MovableUnit(List<LatLng> waypoints, LatLng startingPosition, Path path, Units type, Hostility hostility) {
        super(startingPosition, type, hostility);
        this.path = path;
        currentTarget = 0;
        this.currentPosition = new AtomicReference<>(startingPosition);
        this.lastUpdated = new AtomicLong(getCreatedTime());
        this.distanceTraveled = new AtomicReference<>(0d);
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

    public void move() {
        this.mph.set(startingMph());
    }

    public void setCurrentPosition(LatLng position) {
        if (!position.equals(currentPosition.get())) {
            currentPosition.set(position);
            positionChangeListener.onPositionChange(this);
        }
    }

    public LatLng getCurrentPosition() {
        return currentPosition.get();
    }

    protected void stop() {
        this.mph.set(0d);
    }

    @Override
    public void registerPositionChangeListener(PositionChangeListener positionChangeListener) {
        this.positionChangeListener = positionChangeListener;
    }
}
