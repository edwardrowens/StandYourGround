package com.ede.standyourground.game.api.event.observer;

import com.ede.standyourground.game.api.event.listener.OnDeathListener;

public interface DeathObserver {
    void registerOnDeathListener(OnDeathListener onDeathListener);
}
