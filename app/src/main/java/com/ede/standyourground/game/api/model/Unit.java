package com.ede.standyourground.game.api.model;

import android.os.SystemClock;

import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.game.api.event.listener.HealthChangeListener;
import com.ede.standyourground.game.api.event.listener.OnDeathListener;
import com.ede.standyourground.game.api.event.listener.VisibilityChangeListener;
import com.ede.standyourground.game.api.event.observer.VisibilityChangeObserver;
import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public abstract class Unit implements Attackable, VisibilityChangeObserver {
    private static final Logger logger = new Logger(Unit.class);

    private final AtomicInteger health = new AtomicInteger(getMaxHealth());
    private final AtomicLong createdTime;
    private final AtomicBoolean alive = new AtomicBoolean(true);
    private final LatLng startingPosition;
    private final UUID id = UUID.randomUUID();
    private final Hostility hostility;
    private final double radius;
    private final UnitType type;
    private final AtomicReference<Cell> cell;

    // Listeners
    private OnDeathListener onDeathListener;
    private HealthChangeListener healthChangeListener;
    protected VisibilityChangeListener visibilityChangeListener;

    protected final AtomicBoolean isVisible;

    public abstract int getMaxHealth();
    public abstract double getVisionRadius();
    protected abstract void onUnitDeath();

    public Unit(LatLng startingPosition, UnitType type, Hostility hostility, Cell cell) {
        this.startingPosition = startingPosition;
        this.createdTime = new AtomicLong(SystemClock.uptimeMillis());
        this.hostility = hostility;
//        this.isVisible = new AtomicBoolean(this.hostility == Hostility.FRIENDLY);
        this.isVisible = new AtomicBoolean(true);
        this.radius = type.getSize();
        this.type = type;
        this.cell = new AtomicReference<>(cell);
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

    @Override
    public Hostility getHostility() {
        return hostility;
    }

    public boolean isVisible() {
        return isVisible.get();
    }

    public void setIsVisible(boolean isVisible) {
//        if (isVisible != this.isVisible.get()) {
//            this.isVisible.set(isVisible);
//            visibilityChangeListener.onVisibilityChange(this);
//        }
    }

    @Override
    public void onDeath(Unit killer) {
        logger.i("%s has died. Calling all listeners", getId());
        alive.set(false);
        onUnitDeath();
        onDeathListener.onDeath(this, killer);
    }

    @Override
    public void onAttacked(Attacker attacker) {
        this.deductHealth(attacker.getDamage());
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

    public UnitType getType() {
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

    public Cell getCell() {
        return cell.get();
    }

    public void setCell(Cell cell) {
        this.cell.set(cell);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Unit unit = (Unit) o;

        if (Double.compare(unit.radius, radius) != 0) return false;
        if (!health.equals(unit.health)) return false;
        if (!createdTime.equals(unit.createdTime)) return false;
        if (!alive.equals(unit.alive)) return false;
        if (!startingPosition.equals(unit.startingPosition)) return false;
        if (!id.equals(unit.id)) return false;
        if (hostility != unit.hostility) return false;
        if (type != unit.type) return false;
        if (!cell.equals(unit.cell)) return false;
        if (!onDeathListener.equals(unit.onDeathListener)) return false;
        if (!healthChangeListener.equals(unit.healthChangeListener)) return false;
        if (!visibilityChangeListener.equals(unit.visibilityChangeListener)) return false;
        return isVisible.equals(unit.isVisible);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = health.hashCode();
        result = 31 * result + createdTime.hashCode();
        result = 31 * result + alive.hashCode();
        result = 31 * result + startingPosition.hashCode();
        result = 31 * result + id.hashCode();
        result = 31 * result + hostility.hashCode();
        temp = Double.doubleToLongBits(radius);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + type.hashCode();
        result = 31 * result + cell.hashCode();
        result = 31 * result + isVisible.hashCode();
        return result;
    }
}
