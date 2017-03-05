package com.ede.standyourground.game.model;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;

import com.ede.standyourground.framework.Logger;
import com.ede.standyourground.game.model.api.Attackable;
import com.ede.standyourground.game.model.api.Attacker;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Marauder extends MovableUnit {
    private static final Logger logger = new Logger(Marauder.class);

    private static final double STARTING_MPH = 150;
    private static final int STARTING_HEALTH = 150;
    private static final double ATTACK_SPEED = .5;

    private final double attackRange;

    private long lastAttackTime;

    private final Circle circle;

    public Marauder(List<LatLng> waypoints, LatLng position, Path path, Circle circle, boolean isEnemy) {
        super(waypoints, position, path, 60, isEnemy);
        this.lastAttackTime = 0;
        this.circle = circle;
        this.circle.setRadius(60);
        if (!isEnemy) {
            this.circle.setFillColor(Color.CYAN);
        }
        this.attackRange = circle.getRadius() * 2;
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
            setMph(STARTING_MPH);
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
    public void onRender() {
        circle.setCenter(getCurrentPosition());
        circle.setVisible(isVisible());
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
    protected double startingMph() {
        return STARTING_MPH;
    }
}
