package com.ede.standyourground.game.model;

import android.os.SystemClock;

import com.ede.standyourground.framework.Logger;
import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Eddie on 2/3/2017.
 */

public class Unit {
    private Logger logger = new Logger(Unit.class);
    protected AtomicReference<LatLng> position;
    public final UUID id = UUID.randomUUID();
    private long createdTime;

    public Unit(LatLng position) {
        this.position = new AtomicReference<>(position);
        this.createdTime = SystemClock.uptimeMillis();
    }

    public LatLng getPosition() {
        return position.get();
    }

    public UUID getId() {
        return id;
    }

    public long getCreatedTime() {
        return createdTime;
    }
}
