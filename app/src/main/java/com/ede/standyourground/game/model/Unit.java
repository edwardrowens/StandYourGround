package com.ede.standyourground.game.model;

import android.os.SystemClock;

import com.ede.standyourground.game.model.api.Attackable;
import com.ede.standyourground.game.model.api.DeathListener;
import com.ede.standyourground.game.model.api.Mortal;
import com.ede.standyourground.game.model.api.Renderable;
import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public abstract class Unit implements Renderable, Attackable, Mortal {
    private final LatLng startingPosition;
    private final UUID id = UUID.randomUUID();
    private final AtomicLong createdTime;
    private final boolean isEnemy;
    private final AtomicReference<LatLng> currentPosition;
    protected double visionRadius;
    private final AtomicInteger health;
    private final AtomicBoolean isVisible;
    private final double radius;

    protected DeathListener deathListener;

    public Unit(LatLng startingPosition, int health, double radius, boolean isEnemy) {
        this.startingPosition = startingPosition;
        this.createdTime = new AtomicLong(SystemClock.uptimeMillis());
        this.isEnemy = isEnemy;
        this.visionRadius = 0;
        this.isVisible = new AtomicBoolean(!isEnemy);
        this.currentPosition = new AtomicReference<>(startingPosition);
        this.health = new AtomicInteger(health);
        this.radius = radius;
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

    public void deductHealth(int toDeduct) {
        health.getAndSet(health.get() - toDeduct);
    }

    public int getHealth() {
        return health.get();
    }

    @Override
    public void registerDeathListener(DeathListener deathListener) {
        this.deathListener = deathListener;
    }

    public double getRadius() {
        return radius;
    }
}
