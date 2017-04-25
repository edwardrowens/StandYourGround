package com.ede.standyourground.game.api.service;

import com.ede.standyourground.game.api.event.observer.CoinBalanceChangeObserver;
import com.ede.standyourground.game.api.event.observer.IncomeAccruedObserver;
import com.ede.standyourground.game.api.model.Player;

import java.util.UUID;

/**
 *
 */

public interface PlayerService extends CoinBalanceChangeObserver, IncomeAccruedObserver {
    void addPlayer(Player player);
    void makePurchase(UUID playerId, int costOfItem);
    void updateIncome(UUID playerId, int updateAmount);
    void accrueIncome();
    long getLastResourceAccrual(UUID playerId);
    boolean checkFunds(UUID playerId, int amountToCheck);
    int getCurrentBalance(UUID playerId);
    int getMedicNeutralCampCount(UUID playerId);
    int getBankNeutralCampCount(UUID playerId);
    UUID getMainPlayerId();
}
