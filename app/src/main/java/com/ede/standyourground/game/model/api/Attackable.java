package com.ede.standyourground.game.model.api;


public interface Attackable extends Mortal {
    void onAttacked(Attacker attacker);
    int getHealth();
    boolean isEnemy();
}
