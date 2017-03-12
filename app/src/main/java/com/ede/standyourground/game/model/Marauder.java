package com.ede.standyourground.game.model;

import com.ede.standyourground.app.activity.MapsActivity;
import com.ede.standyourground.framework.Logger;
import com.ede.standyourground.game.model.api.Attackable;
import com.ede.standyourground.game.model.api.Attacker;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Marauder extends MovableUnit {
    private static final Logger logger = new Logger(Marauder.class);

    private static final double STARTING_MPH = 150;
    private static final int STARTING_HEALTH = 150;
    private static final double ATTACK_SPEED = .5;

    private final double attackRange;

    private long lastAttackTime;

    public Marauder(List<LatLng> waypoints, LatLng position, Path path, double radius, boolean isEnemy) {
        super(waypoints, position, path, 60, Units.MARAUDER, isEnemy);
        this.lastAttackTime = 0;
        this.attackRange = radius * 2;
    }

    @Override
    public void onAttacked(Attacker attacker) {
        this.deductHealth(attacker.getDamage());
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
        return 5;
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
                && (attackable.isEnemy() != isEnemy())
                && attackable.isAlive()
                && isAlive();
    }

    @Override
    public void onRender() {
        MapsActivity.getCircles().get(getId()).setCenter(getCurrentPosition());
        MapsActivity.getCircles().get(getId()).setVisible(isVisible());
    }

    @Override
    public int getMaxHealth() {
        return STARTING_HEALTH;
    }

    @Override
    public double getVisionRadius() {
        return 321.869; // .2 miles
    }

    @Override
    protected void onUnitDeath() {
        MapsActivity.removeCircle(getId());
    }

    @Override
    protected double startingMph() {
        return STARTING_MPH;
    }
}
