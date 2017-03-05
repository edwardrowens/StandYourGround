package com.ede.standyourground.game.model;


import com.ede.standyourground.game.model.api.Attacker;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;

public class Base extends Unit {
    private static final int HEALTH = 100;

    private final Circle circle;

    public Base(LatLng position, Circle circle, boolean isEnemy) {
        super(position, 100, isEnemy);
        this.circle = circle;
        circle.setRadius(100);
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
    public int getMaxHealth() {
        return HEALTH;
    }

    @Override
    public double getVisionRadius() {
        return 160.934; //.1 miles
    }

    @Override
    public boolean isVisible() {
        return true;
    }
}
