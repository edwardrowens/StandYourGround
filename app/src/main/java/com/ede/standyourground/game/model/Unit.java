package com.ede.standyourground.game.model;

import android.os.SystemClock;

import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Eddie on 2/3/2017.
 */

public class Unit {
    protected LatLng position;
    private AtomicLong arrivalTime = new AtomicLong();
    public final UUID id = UUID.randomUUID();

    public Unit(LatLng position) {
        this.position = position;
        this.arrivalTime.set(SystemClock.uptimeMillis());
    }

    public LatLng getPosition() {
        return position;
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
