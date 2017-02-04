package com.ede.standyourground.game.model;

import android.os.SystemClock;

import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;

/**
 * Created by Eddie on 2/3/2017.
 */

public class Unit {
    protected LatLng position;
    private long createdTime;
    public final UUID id = UUID.randomUUID();

    public Unit(LatLng position) {
        this.position = position;
        this.createdTime = SystemClock.uptimeMillis();
    }

    public LatLng getPosition() {
        return position;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public UUID getId() {
        return id;
    }
}
