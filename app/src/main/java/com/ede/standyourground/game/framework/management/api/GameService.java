package com.ede.standyourground.game.framework.management.api;

/**
 *
 */

public interface GameService extends GameEndObserver {
    void startGame();
    void stopGame();
}
