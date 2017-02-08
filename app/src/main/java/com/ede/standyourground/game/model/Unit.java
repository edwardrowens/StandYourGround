package com.ede.standyourground.game.model;

import android.os.SystemClock;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;

import java.util.UUID;

/**
 * Created by Eddie on 2/3/2017.
 */

public class Unit {
    private final LatLng startingPosition;
    private final UUID id = UUID.randomUUID();
    private final long createdTime;
    private final Marker marker;
    private final Polyline polyline;

    public Unit(LatLng startingPosition, Marker marker, Polyline polyline) {
        this.startingPosition = startingPosition;
        this.createdTime = SystemClock.uptimeMillis();
        this.marker = marker;
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

    public Marker getMarker() {
        return marker;
    }
}
