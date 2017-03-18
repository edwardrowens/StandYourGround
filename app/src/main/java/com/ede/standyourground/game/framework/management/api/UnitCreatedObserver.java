package com.ede.standyourground.game.framework.management.api;

import com.ede.standyourground.game.model.api.UnitCreatedListener;

/**
 *
 */

public interface UnitCreatedObserver {
    void registerUnitCreatedListener(UnitCreatedListener unitCreatedListener);
}
