package com.ede.standyourground.game.model.api;


import com.ede.standyourground.game.framework.management.api.HealthChangeObserver;

public interface Attackable extends Mortal, HealthChangeObserver {
    void onAttacked(Attacker attacker);
    boolean isEnemy();
    double getRadius();
    boolean isAlive();
}
