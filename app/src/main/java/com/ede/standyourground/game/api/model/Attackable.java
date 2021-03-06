package com.ede.standyourground.game.api.model;


import com.ede.standyourground.game.api.event.observer.HealthChangeObserver;

public interface Attackable extends Mortal, HealthChangeObserver {
    void onAttacked(Attacker attacker);
    Hostility getHostility();
    double getRadius();
    boolean isAlive();
}
