package com.ede.standyourground.game.framework.management.api;

import com.ede.standyourground.game.model.api.DeathListener;

public interface DeathObserver {
    void registerDeathListener(DeathListener deathListener);
}
