package com.ede.standyourground.game.model;


import com.ede.standyourground.game.model.api.Attacker;
import com.google.android.gms.maps.model.LatLng;

public class Base extends Unit {
    private static final int HEALTH = 100;
    private static final double RADIUS = 10d;

    public Base(LatLng position, boolean isEnemy) {
        super(position, RADIUS, isEnemy);
    }

    @Override
    public void onRender() {

    }

    @Override
    public void onAttacked(Attacker attacker) {
        deductHealth(attacker.getDamage());
    }

    @Override
    public void onDeath() {
        deathListener.onDeath(this);
    }

    @Override
    protected int startingHealth() {
        return HEALTH;
    }

    @Override
    public double getVisionRadius() {
        return .1;
    }
}
