package com.ede.standyourground.game.api.model;


import com.ede.standyourground.game.api.event.observer.DeathObservable;

public interface Mortal extends DeathObservable {
    void onDeath(Unit killer);
}
