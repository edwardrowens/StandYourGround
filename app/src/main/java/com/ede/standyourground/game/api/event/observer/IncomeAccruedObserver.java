package com.ede.standyourground.game.api.event.observer;

import com.ede.standyourground.game.api.event.listener.IncomeAccruedListener;

/**
 *
 */

public interface IncomeAccruedObserver {
    void registerIncomeAccruedListener(IncomeAccruedListener incomeAccruedListener);
}
