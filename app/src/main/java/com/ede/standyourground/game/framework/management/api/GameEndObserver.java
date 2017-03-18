package com.ede.standyourground.game.framework.management.api;

import com.ede.standyourground.game.model.api.GameEndListener;

/**
 *
 */

public interface GameEndObserver {
    void registerGameEndListener(GameEndListener gameEndListener);
}
