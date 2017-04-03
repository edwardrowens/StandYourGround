package com.ede.standyourground.game.impl.service;

import com.ede.standyourground.game.api.event.listener.CoinBalanceChangeListener;
import com.ede.standyourground.game.api.event.listener.IncomeAccruedListener;
import com.ede.standyourground.game.api.model.Player;
import com.ede.standyourground.game.api.service.PlayerService;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 *
 */
@Singleton
public class PlayerServiceImpl implements PlayerService {

    private Player player;

    // Listeners
    private final List<CoinBalanceChangeListener> coinBalanceChangeListeners = new CopyOnWriteArrayList<>();
    private final List<IncomeAccruedListener> incomeAccruedListeners = new CopyOnWriteArrayList<>();

    @Inject
    PlayerServiceImpl() {
    }

    @Override
    public void addPlayer(Player player) {
        player.registerCoinBalanceChangeListener(new CoinBalanceChangeListener() {
            @Override
            public void onCoinBalanceChange(int oldBalance, int newBalance) {
                for (CoinBalanceChangeListener coinBalanceChangeListener : coinBalanceChangeListeners) {
                    coinBalanceChangeListener.onCoinBalanceChange(oldBalance, newBalance);
                }
            }
        });
        this.player = player;
        player.setLastResourceAccrual(System.currentTimeMillis());
    }

    @Override
    public void makePurchase(int costOfItem) {
        if (checkFunds(costOfItem)) {
            player.updateCoins(-costOfItem);
        }
    }

    @Override
    public void updateIncome(int updateAmount) {
        player.updateIncome(updateAmount);
    }

    @Override
    public void accrueIncome() {
        player.setLastResourceAccrual(System.currentTimeMillis());
        player.updateCoins(player.getIncome());

        for (IncomeAccruedListener incomeAccruedListener : incomeAccruedListeners) {
            incomeAccruedListener.onIncomeAccrued();
        }
    }

    @Override
    public long getLastResourceAccrual() {
        return player.getLastResourceAccrual();
    }

    @Override
    public boolean checkFunds(int amountToCheck) {
        return player.getCoins() >= amountToCheck;
    }

    @Override
    public int getCurrentBalance() {
        return player.getCoins();
    }

    @Override
    public void registerCoinBalanceChangeListener(CoinBalanceChangeListener coinBalanceChangeListener) {
        coinBalanceChangeListeners.add(coinBalanceChangeListener);
    }

    @Override
    public void registerIncomeAccruedListener(IncomeAccruedListener incomeAccruedListener) {
        incomeAccruedListeners.add(incomeAccruedListener);
    }
}
