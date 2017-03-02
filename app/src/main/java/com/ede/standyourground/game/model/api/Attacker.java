package com.ede.standyourground.game.model.api;


public interface Attacker {
    void onAttack(Attackable attackable, double distance);
    int getDamage();
    double getAttackRange();
    double getAttackSpeed();
    boolean canAttack(Attackable attackable, double distance);
}
