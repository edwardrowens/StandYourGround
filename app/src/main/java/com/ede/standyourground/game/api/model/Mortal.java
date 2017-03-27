package com.ede.standyourground.game.api.model;


import com.ede.standyourground.game.api.event.observer.DeathObserver;

public interface Mortal extends DeathObserver {
    void onDeath(Unit killer);
}
