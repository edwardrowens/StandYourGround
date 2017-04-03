package com.ede.standyourground.game.api.event.observer;

import com.ede.standyourground.game.api.event.listener.CoinBalanceChangeListener;

/**
 *
 */

public interface CoinBalanceChangeObserver {
    void registerCoinBalanceChangeListener(CoinBalanceChangeListener coinBalanceChangeListener);
}
