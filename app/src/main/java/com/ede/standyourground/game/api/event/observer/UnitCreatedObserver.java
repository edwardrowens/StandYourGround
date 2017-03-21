package com.ede.standyourground.game.api.event.observer;

import com.ede.standyourground.game.api.event.listener.UnitCreatedListener;

/**
 *
 */

public interface UnitCreatedObserver {
    void registerUnitCreatedListener(UnitCreatedListener unitCreatedListener);
}
