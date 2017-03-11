package com.ede.standyourground.game.model;


import com.ede.standyourground.app.activity.MapsActivity;
import com.ede.standyourground.game.model.api.Attacker;
import com.google.android.gms.maps.model.LatLng;

public class Base extends Unit {
    private static final int HEALTH = 100;

    public Base(LatLng position, boolean isEnemy) {
        super(position, 100, Units.BASE, isEnemy);
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
        MapsActivity.removeCircle(getId());
    }

    @Override
    public boolean isVisible() {
        return true;
    }
}
