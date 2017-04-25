package com.ede.standyourground.game.impl.model;


import com.ede.standyourground.game.api.model.Cell;
import com.ede.standyourground.game.api.model.Hostility;
import com.ede.standyourground.game.api.model.Unit;
import com.ede.standyourground.game.api.model.UnitType;
import com.google.android.gms.maps.model.LatLng;

public class Base extends Unit {
    private static final int HEALTH = 100;

    public Base(LatLng position, Hostility hostility, Cell cell) {
        super(position, UnitType.BASE, hostility, cell);
        isVisible.set(true);
    }

    @Override
    public int getMaxHealth() {
        return HEALTH;
    }

    @Override
    public double getVisionRadius() {
        return 1000; //.1 miles
    }

    @Override
    protected void onUnitDeath() {
    }

    @Override
    public boolean isVisible() {
        return true;
    }
}
