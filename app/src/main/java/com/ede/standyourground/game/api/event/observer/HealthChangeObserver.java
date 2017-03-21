package com.ede.standyourground.game.api.event.observer;

import com.ede.standyourground.game.api.event.listener.HealthChangeListener;

/**
 *
 */

public interface HealthChangeObserver {
    void registerHealthChangeListener(HealthChangeListener healthChangeListener);
}
