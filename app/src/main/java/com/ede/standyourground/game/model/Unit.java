package com.ede.standyourground.game.model;

import android.os.SystemClock;

import com.ede.standyourground.game.framework.render.api.Renderable;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public abstract class Unit implements Renderable {
    private final LatLng startingPosition;
    private final UUID id = UUID.randomUUID();
    private AtomicLong createdTime;
    private final List<LatLng> waypoints;

    public Unit(LatLng startingPosition, List<LatLng> waypoints) {
        this.startingPosition = startingPosition;
        this.createdTime = new AtomicLong(SystemClock.uptimeMillis());
        this.waypoints = waypoints;
    }

    public LatLng getStartingPosition() {
        return startingPosition;
    }

    public UUID getId() {
        return id;
    }

    public long getCreatedTime() {
        return createdTime.get();
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime.set(createdTime);
    }

    public List<LatLng> getWaypoints() {
        return waypoints;
    }
}
