package com.ede.standyourground.game.api.event.observer;

import com.ede.standyourground.game.api.event.listener.VisibilityChangeListener;

/**
 *
 */

public interface VisibilityChangeObserver {
    void registerVisibilityChangeListener(VisibilityChangeListener visibilityChangeListener);
}
