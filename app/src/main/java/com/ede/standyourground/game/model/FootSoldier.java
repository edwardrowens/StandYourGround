package com.ede.standyourground.game.model;

import android.os.Handler;
import android.os.Looper;

import com.ede.standyourground.game.model.api.Attackable;
import com.ede.standyourground.game.model.api.Attacker;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class FootSoldier extends MovableUnit {

    private static final double DEFAULT_MPH = 150;
    private static final int DEFAULT_HEALTH = 50;
    private static final double ATTACK_SPEED = .5;

    private final Circle circle;
    private final double attackRange;

    private long lastAttackTime;

    public FootSoldier(List<LatLng> waypoints, LatLng position, Path path, boolean isEnemy, Circle circle) {
        super(waypoints, position, path, circle.getRadius(), isEnemy);
        this.circle = circle;
        this.lastAttackTime = 0;
        this.attackRange = circle.getRadius() * 2;
    }

    @Override
    public void onRender() {
        circle.setCenter(getCurrentPosition());
        circle.setVisible(isVisible());
    }

    @Override
    public void onAttacked(Attacker attacker) {
        this.deductHealth(attacker.getDamage());
    }

    @Override
    public void onAttack(final Attackable attackable, double distance) {
        if (canAttack(attackable, distance)) {
            setMph(0);
            attackable.onAttacked(this);
            lastAttackTime = System.currentTimeMillis();
        }

        if (attackable.getHealth() <= 0) {
            setMph(DEFAULT_MPH);
            // This must be posted to the main looper because a unit may be manipulating
            // a google maps object (which can't be manipulated by a foreign thread) on death.
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    attackable.onDeath();
                }
            });
        }
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
        return (System.currentTimeMillis() - lastAttackTime) > (1000 / getAttackSpeed())
                && (distance <= getAttackRange() + attackable.getRadius())
                && ((isEnemy() && !attackable.isEnemy()) || (!isEnemy() && attackable.isEnemy()));
    }

    @Override
    public void onDeath() {
        circle.remove();
        deathListener.onDeath(this);
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
}
