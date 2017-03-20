package com.ede.standyourground.game.model.api;


import com.ede.standyourground.game.framework.management.api.DeathObserver;

public interface Mortal extends DeathObserver {
    void onDeath();
}
