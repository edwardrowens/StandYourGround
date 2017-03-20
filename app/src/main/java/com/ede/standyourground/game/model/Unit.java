package com.ede.standyourground.game.model;

import android.os.SystemClock;

import com.ede.standyourground.framework.Logger;
import com.ede.standyourground.game.model.api.Attackable;
import com.ede.standyourground.game.model.api.HealthChangeListener;
import com.ede.standyourground.game.model.api.OnDeathListener;
import com.ede.standyourground.game.model.api.Renderable;
import com.ede.standyourground.game.model.api.VisibilityChangeListener;
import com.ede.standyourground.game.model.api.VisibilityChangeObserver;
import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public abstract class Unit implements Renderable, Attackable, VisibilityChangeObserver {

    private static final Logger logger = new Logger(Unit.class);

    private final AtomicInteger health = new AtomicInteger(getMaxHealth());
    private final AtomicLong createdTime;
    private final AtomicBoolean alive = new AtomicBoolean(true);
    private final LatLng startingPosition;
    private final UUID id = UUID.randomUUID();
    private final boolean isEnemy;
    private final double radius;
    private final Units type;
    // Listeners
    private OnDeathListener onDeathListener;
    private HealthChangeListener healthChangeListener;
    private VisibilityChangeListener visibilityChangeListener;

    protected final AtomicBoolean isVisible;

    public abstract int getMaxHealth();
    public abstract double getVisionRadius();
    protected abstract void onUnitDeath();

    public Unit(LatLng startingPosition, double radius, Units type, boolean isEnemy) {
        this.startingPosition = startingPosition;
        this.createdTime = new AtomicLong(SystemClock.uptimeMillis());
        this.isEnemy = isEnemy;
        this.isVisible = new AtomicBoolean(!isEnemy);
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

    public boolean isEnemy() {
        return isEnemy;
    }

    public boolean isVisible() {
        return isVisible.get();
    }

    public void setIsVisible(boolean isVisible) {
        if (isVisible != this.isVisible.get()) {
            this.isVisible.set(isVisible);
            visibilityChangeListener.onVisibilityChange(this);
        }
    }

    @Override
    public void onDeath() {
        logger.i("%s has died.", getId());
        alive.set(false);
        onUnitDeath();
        onDeathListener.onDeath(Unit.this);
    }

    public double getRadius() {
        return radius;
    }

    public int getHealth() {
        return health.get();
    }

    public void deductHealth(int toDeduct) {
        health.addAndGet(-toDeduct);
        healthChangeListener.onHealthChange(this);
    }

    public Units getType() {
        return type;
    }

    @Override
    public boolean isAlive() {
        return alive.get() && getHealth() > 0;
    }

    @Override
    public void registerHealthChangeListener(HealthChangeListener healthChangeListener) {
        this.healthChangeListener = healthChangeListener;
    }

    @Override
    public void registerOnDeathListener(OnDeathListener onDeathListener) {
        this.onDeathListener = onDeathListener;
    }

    @Override
    public void registerVisibilityChangeListener(VisibilityChangeListener visibilityChangeListener) {
        this.visibilityChangeListener = visibilityChangeListener;
    }
}
