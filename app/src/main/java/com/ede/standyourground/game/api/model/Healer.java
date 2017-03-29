package com.ede.standyourground.game.api.model;

/**
 *
 */

public interface Healer {
    boolean heal(Healable healable);
    boolean canHeal(Unit unit, double distance);
}
