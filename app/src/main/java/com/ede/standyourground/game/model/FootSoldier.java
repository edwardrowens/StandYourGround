package com.ede.standyourground.game.model;

import com.ede.standyourground.game.model.api.Attackable;
import com.ede.standyourground.game.model.api.Attacker;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class FootSoldier extends MovableUnit {

    private final Circle circle;
    private static final int HEALTH = 10;
    private static final double MPH = 100;
    private static final double ATTACK_SPEED = .5;
    private long lastAttackTime;

    public FootSoldier(List<LatLng> waypoints, LatLng position, Path path, boolean isEnemy, Circle circle) {
        super(MPH, waypoints, position, path, HEALTH, circle.getRadius(), isEnemy);
        this.circle = circle;
        this.visionRadius = 1;
        this.lastAttackTime = 0;
    }

    @Override
    public void onRender() {
        circle.setCenter(getCurrentPosition());
        circle.setVisible(isVisible());
    }

    @Override
    public void onAttacked(Attacker attacker) {
        deductHealth(attacker.getDamage());
    }

    @Override
    public void onAttack(Attackable attackable) {
        if (canAttack()) {
            attackable.onAttacked(this);
            lastAttackTime = System.currentTimeMillis();
        }
    }

    @Override
    public int getDamage() {
        return 3;
    }

    @Override
    public double getAttackRange() {
        return getRadius() + 10;
    }

    @Override
    public double getAttackSpeed() {
        return ATTACK_SPEED;
    }

    @Override
    public boolean canAttack() {
        return (System.currentTimeMillis() - lastAttackTime) > (1000 / ATTACK_SPEED);
    }

    @Override
    public void onDeath() {
        circle.remove();
        deathListener.onDeath(this);
    }
}
