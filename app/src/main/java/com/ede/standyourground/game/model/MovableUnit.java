package com.ede.standyourground.game.model;

import android.os.SystemClock;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

import java.util.concurrent.atomic.AtomicReference;


/**
 * Created by Eddie on 2/3/2017.
 */

public class MovableUnit extends Unit implements Movable {

    private int speed;
    private long createdTime;
    private Polyline path;
    private AtomicReference<LatLng> position;

    public MovableUnit(int speed, Polyline path, LatLng position) {
        this.speed = speed;
        this.path = path;
        createdTime = SystemClock.uptimeMillis();
        this.position = new AtomicReference<>(position);
    }

    @Override
    public void move(long elapsedTime) {
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public int getSpeed() {
        return speed;
    }

    @Override
    public LatLng getPosition() {
        return position.get();
    }

    @Override
    public void setPosition(LatLng position) {
        this.position.set(position);
    }
}
