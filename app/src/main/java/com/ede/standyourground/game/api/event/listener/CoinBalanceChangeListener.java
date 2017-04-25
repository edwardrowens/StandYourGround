package com.ede.standyourground.game.api.event.listener;

import com.ede.standyourground.game.api.model.Player;

/**
 *
 */

public interface CoinBalanceChangeListener {
    void onCoinBalanceChange(Player player, int oldBalance, int newBalance);
}
