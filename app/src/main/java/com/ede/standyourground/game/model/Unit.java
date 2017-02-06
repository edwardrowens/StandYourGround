package com.ede.standyourground.game.model;

import android.os.SystemClock;

import com.ede.standyourground.framework.Logger;
import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Eddie on 2/3/2017.
 */

public class Unit {
    private Logger logger = new Logger(Unit.class);
    protected AtomicReference<LatLng> position;
    private AtomicLong arrivalTime = new AtomicLong();
    public final UUID id = UUID.randomUUID();

    public Unit(LatLng position) {
        this.position = new AtomicReference<>(position);
        this.arrivalTime.set(SystemClock.uptimeMillis());
    }

    public LatLng getPosition() {
        return position.get();
    }

    public long getArrivalTime() {
        return arrivalTime.get();
    }

    public void setArrivalTime(long arrivalTime) {
        this.arrivalTime.set(arrivalTime);
    }

    public UUID getId() {
        return id;
    }
}
