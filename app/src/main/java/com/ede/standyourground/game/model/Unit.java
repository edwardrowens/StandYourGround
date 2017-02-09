package com.ede.standyourground.game.model;

import android.os.SystemClock;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

import java.util.UUID;

/**
 *
 * Created by Eddie on 2/3/2017.
 */

public class Unit {
    private final LatLng startingPosition;
    private final UUID id = UUID.randomUUID();
    private final long createdTime;
    private final Circle circle;
    private final Polyline polyline;

    public Unit(LatLng startingPosition, Circle circle, Polyline polyline) {
        this.startingPosition = startingPosition;
        this.createdTime = SystemClock.uptimeMillis();
        this.circle = circle;
        this.polyline = polyline;
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

    public Polyline getPolyline() {
        return polyline;
    }

    public Circle getCircle() {
        return circle;
    }
}
