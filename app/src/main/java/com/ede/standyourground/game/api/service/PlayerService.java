package com.ede.standyourground.game.api.service;

import com.ede.standyourground.game.api.event.observer.CoinBalanceChangeObserver;
import com.ede.standyourground.game.api.event.observer.IncomeAccruedObserver;
import com.ede.standyourground.game.api.model.Player;

/**
 *
 */

public interface PlayerService extends CoinBalanceChangeObserver, IncomeAccruedObserver {
    void addPlayer(Player player);
    void makePurchase(int costOfItem);
    void updateIncome(int updateAmount);
    void accrueIncome();
    long getLastResourceAccrual();
    boolean checkFunds(int amountToCheck);
    int getCurrentBalance();
    int getMedicNeutralCampCount();
    int getBankNeutralCampCount();
}
