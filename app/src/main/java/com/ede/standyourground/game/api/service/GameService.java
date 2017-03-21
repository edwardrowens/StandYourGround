package com.ede.standyourground.game.api.service;

import com.ede.standyourground.game.api.event.observer.GameEndObserver;

/**
 *
 */

public interface GameService extends GameEndObserver {
    void startGame();
    void stopGame();
}
