package com.ede.standyourground.game.impl.model;

import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.game.api.model.Attackable;
import com.ede.standyourground.game.api.model.Attacker;
import com.ede.standyourground.game.api.model.Cell;
import com.ede.standyourground.game.api.model.Healable;
import com.ede.standyourground.game.api.model.Hostility;
import com.ede.standyourground.game.api.model.MovableUnit;
import com.ede.standyourground.game.api.model.Path;
import com.ede.standyourground.game.api.model.UnitType;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class FootSoldier extends MovableUnit implements Attacker, Healable {

    private static final Logger logger = new Logger(FootSoldier.class);

    private static final double DEFAULT_MPH = 150;
    private static final int DEFAULT_HEALTH = 50;
    private static final double ATTACK_SPEED = .5;

    private final double attackRange;

    private long lastAttackTime;

    public FootSoldier(List<LatLng> waypoints, LatLng position, Path path, Hostility hostility, Cell cell) {
        super(waypoints, position, path, UnitType.FOOT_SOLDIER, hostility, cell);
        this.lastAttackTime = 0;
        this.attackRange = getRadius() * 2;
    }

    @Override
    public boolean combat(final Attackable attackable) {
        this.stop();
        if ((System.currentTimeMillis() - lastAttackTime) > (1000 / getAttackSpeed())) {
            attackable.onAttacked(this);
            lastAttackTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    @Override
    public int getDamage() {
        return 3;
    }

    @Override
    public double getAttackRange() {
        return attackRange;
    }

    @Override
    public double getAttackSpeed() {
        return ATTACK_SPEED;
    }

    @Override
    public boolean canAttack(Attackable attackable, double distance) {
        return (distance <= getAttackRange() + attackable.getRadius())
                && (attackable.getHostility() != getHostility())
                && attackable.isAlive()
                && isAlive();
    }

    @Override
    protected double startingMph() {
        return DEFAULT_MPH;
    }

    @Override
    public int getMaxHealth() {
        return DEFAULT_HEALTH;
    }

    @Override
    public double getVisionRadius() {
        return 160.934; //.1 miles
    }

    @Override
    protected void onUnitDeath() {

    }

    @Override
    public void incrementHealth(int value) {
        deductHealth(-value);
    }
}
