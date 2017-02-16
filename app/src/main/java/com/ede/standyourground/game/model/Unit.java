package com.ede.standyourground.game.model;

import android.os.SystemClock;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.UUID;

/**
 *
 * Created by Eddie on 2/3/2017.
 */

public class Unit {
    private final LatLng startingPosition;
    private final UUID id = UUID.randomUUID();
    private long createdTime;
    private final List<LatLng> waypoints;

    public Unit(LatLng startingPosition, List<LatLng> waypoints) {
        this.startingPosition = startingPosition;
        this.createdTime = SystemClock.uptimeMillis();
        this.waypoints = waypoints;
    }

    public LatLng getStartingPosition() {
        return startingPosition;
    }

    public UUID getId() {
        return id;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public List<LatLng> getWaypoints() {
        return waypoints;
    }
}
