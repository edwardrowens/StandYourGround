package com.ede.standyourground.game.framework.management.api;

import com.ede.standyourground.game.model.api.HealthChangeListener;

/**
 *
 */

public interface HealthChangeObserver {
    void registerHealthChangeListener(HealthChangeListener healthChangeListener);
}
