package com.ede.standyourground.game.model;

import android.os.SystemClock;

import com.ede.standyourground.game.framework.render.api.Renderable;
import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public abstract class Unit implements Renderable {
    private final LatLng startingPosition;
    private final UUID id = UUID.randomUUID();
    private AtomicLong createdTime;
    private final boolean isEnemy;
    private final AtomicReference<LatLng> currentPosition;

    private AtomicBoolean isVisible;
    protected double visionRadius;

    public Unit(LatLng startingPosition, boolean isEnemy) {
        this.startingPosition = startingPosition;
        this.createdTime = new AtomicLong(SystemClock.uptimeMillis());
        this.isEnemy = isEnemy;
        this.visionRadius = 0;
        this.isVisible = new AtomicBoolean(!isEnemy);
        this.currentPosition = new AtomicReference<>(startingPosition);
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

    public boolean isEnemy() {
        return isEnemy;
    }

    public double getVisionRadius() {
        return visionRadius;
    }

    public boolean isVisible() {
        return isVisible.get();
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible.set(isVisible);
    }

    public void setCurrentPosition(LatLng position) {
        currentPosition.set(position);
    }

    public LatLng getCurrentPosition() {
        return currentPosition.get();
    }
}
