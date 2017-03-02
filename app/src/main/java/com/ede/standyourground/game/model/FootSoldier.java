package com.ede.standyourground.game.model;

import android.os.Handler;
import android.os.Looper;

import com.ede.standyourground.framework.Logger;
import com.ede.standyourground.game.model.api.Attackable;
import com.ede.standyourground.game.model.api.Attacker;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class FootSoldier extends MovableUnit {

    private static final Logger logger = new Logger(FootSoldier.class);

    private final Circle circle;
    private static final int HEALTH = 10;
    private static final double MPH = 150;
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
    public void onAttack(final Attackable attackable, double distance) {
        if (canAttack(attackable, distance)) {
            logger.d("%s attacking %s", getId(), ((Unit) attackable).getId());
            setMph(0);
            attackable.onAttacked(this);
            long temp = lastAttackTime;
            lastAttackTime = System.currentTimeMillis();
            logger.d("attack: %d", lastAttackTime - temp);
        }

        if (attackable.getHealth() <= 0) {
            setMph(MPH);
            // This must be posted to the main looper because a unit may be manipulating
            // a google maps object (which can't be manipulated by a foreign thread).
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
        return getRadius()*2;
    }

    @Override
    public double getAttackSpeed() {
        return ATTACK_SPEED;
    }

    @Override
    public boolean canAttack(Attackable attackable, double distance) {
        return (System.currentTimeMillis() - lastAttackTime) > (1000 / getAttackSpeed())
                && distance <= getAttackRange()
                && ((isEnemy() && !attackable.isEnemy()) || (!isEnemy() && attackable.isEnemy()));
    }

    @Override
    public void onDeath() {
        circle.remove();
        deathListener.onDeath(this);
    }
}
