package com.ede.standyourground.game.model.api;


public interface Attacker {
    void onAttack(Attackable attackable);
    int getDamage();
    double getAttackRange();
    double getAttackSpeed();
    boolean canAttack();
}
