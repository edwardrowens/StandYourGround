package com.ede.standyourground.game.framework.management.api;

import com.ede.standyourground.game.model.api.OnDeathListener;

public interface DeathObserver {
    void registerOnDeathListener(OnDeathListener onDeathListener);
}
