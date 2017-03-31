package com.ede.standyourground.game.api.event.observer;

import com.ede.standyourground.game.api.event.listener.OnDeathListener;

/**
 *
 */

public interface DeathObservable {
    void registerOnDeathListener(OnDeathListener onDeathListener);
}
