package com.ede.standyourground.game.impl.model;

import com.ede.standyourground.game.api.model.Healable;
import com.ede.standyourground.game.api.model.Healer;
import com.ede.standyourground.game.api.model.Hostility;
import com.ede.standyourground.game.api.model.MovableUnit;
import com.ede.standyourground.game.api.model.Path;
import com.ede.standyourground.game.api.model.Unit;
import com.ede.standyourground.game.api.model.Units;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 *
 */

public class Medic extends MovableUnit implements Healer {

    private static final int HEAL_AMOUNT = 10;
    private static final double HEALS_PER_SECOND = .5;

    private final double healRange;

    private long lastHealTime;

    public Medic(List<LatLng> waypoints, LatLng startingPosition, Path path, Hostility hostility) {
        super(waypoints, startingPosition, path, Units.MEDIC, hostility);
        healRange = getRadius() * 2;
    }

    @Override
    protected double startingMph() {
        return 150;
    }

    @Override
    public int getMaxHealth() {
        return 40;
    }

    @Override
    public double getVisionRadius() {
        return 160.934;
    }

    @Override
    protected void onUnitDeath() {

    }

    @Override
    public boolean heal(Healable healable) {
        this.stop();
        if ((System.currentTimeMillis() - lastHealTime) > (1000 / HEALS_PER_SECOND)) {
            int missingHealth = healable.getMaxHealth() - healable.getHealth();
            int healAmount = missingHealth >= HEAL_AMOUNT ? HEAL_AMOUNT : HEAL_AMOUNT - missingHealth;
            healable.incrementHealth(healAmount);
            lastHealTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    @Override
    public boolean canHeal(Unit unit, double distance) {
        return distance <= healRange + unit.getRadius()
                && unit.getHostility() == getHostility()
                && unit.isAlive()
                && isAlive()
                && unit instanceof Healable;
    }
}
