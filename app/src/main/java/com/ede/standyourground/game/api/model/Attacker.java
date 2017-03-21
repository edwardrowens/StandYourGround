package com.ede.standyourground.game.api.model;


public interface Attacker {
    /**
     * Engages in combat with an {@link Attackable}.
     *
     * @param attackable Something to attack.
     * @return true if the attackable was attacked (damage was inflicted). Otherwise, return false
     */
    boolean combat(Attackable attackable);
    int getDamage();
    double getAttackRange();
    double getAttackSpeed();
    boolean canAttack(Attackable attackable, double distance);
}
