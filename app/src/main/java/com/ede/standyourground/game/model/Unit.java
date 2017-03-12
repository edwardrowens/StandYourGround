package com.ede.standyourground.game.model;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import com.ede.standyourground.framework.Logger;
import com.ede.standyourground.game.model.api.Attackable;
import com.ede.standyourground.game.model.api.DeathListener;
import com.ede.standyourground.game.model.api.Renderable;
import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public abstract class Unit implements Renderable, Attackable {

    private static final Logger logger = new Logger(Unit.class);

    private final LatLng startingPosition;
    private final UUID id = UUID.randomUUID();
    private final AtomicLong createdTime;
    private final boolean isEnemy;
    private final AtomicReference<LatLng> currentPosition;
    private final AtomicBoolean isVisible;
    private final double radius;
    private final AtomicInteger health = new AtomicInteger(getMaxHealth());
    private final Units type;
    private final AtomicBoolean alive = new AtomicBoolean(true);

    protected DeathListener deathListener;

    public abstract int getMaxHealth();
    public abstract double getVisionRadius();
    protected abstract void onUnitDeath();

    public Unit(LatLng startingPosition, double radius, Units type, boolean isEnemy) {
        this.startingPosition = startingPosition;
        this.createdTime = new AtomicLong(SystemClock.uptimeMillis());
        this.isEnemy = isEnemy;
        this.isVisible = new AtomicBoolean(!isEnemy);
        this.currentPosition = new AtomicReference<>(startingPosition);
        this.radius = radius;
        this.type = type;
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

    @Override
    public void registerDeathListener(DeathListener deathListener) {
        this.deathListener = deathListener;
    }

    @Override
    public void onDeath() {
        logger.i("%s has died.", getId());
        alive.set(false);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                onUnitDeath();
                deathListener.onDeath(Unit.this);
            }
        });
    }

    public double getRadius() {
        return radius;
    }

    public int getHealth() {
        return health.get();
    }

    public void deductHealth(int toDeduct) {
        health.addAndGet(-toDeduct);
    }

    public Units getType() {
        return type;
    }

    @Override
    public boolean isAlive() {
        return alive.get() && getHealth() > 0;
    }
}
