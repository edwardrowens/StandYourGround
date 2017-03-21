package com.ede.standyourground.game.api.event.observer;

import com.ede.standyourground.game.api.event.listener.PositionChangeListener;

/**
 *
 */

public interface PositionChangeObserver {
    void registerPositionChangeListener(PositionChangeListener positionChangeListener);
}
