package com.ede.standyourground.game.impl.model;


import com.ede.standyourground.game.api.model.Attacker;
import com.ede.standyourground.game.api.model.Unit;
import com.ede.standyourground.game.api.model.Units;
import com.google.android.gms.maps.model.LatLng;

public class Base extends Unit {
    private static final int HEALTH = 100;

    public Base(LatLng position, boolean isEnemy) {
        super(position, 100, Units.BASE, isEnemy);
        isVisible.set(true);
    }

    @Override
    public void onRender() {

    }

    @Override
    public void onAttacked(Attacker attacker) {
        deductHealth(attacker.getDamage());
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
    protected void onUnitDeath() {
    }

    @Override
    public boolean isVisible() {
        return true;
    }
}
