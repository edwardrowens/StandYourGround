package com.ede.standyourground.game.api.event.observer;

import com.ede.standyourground.game.api.event.listener.GameEndListener;

/**
 *
 */

public interface GameEndObserver {
    void registerGameEndListener(GameEndListener gameEndListener);
}
