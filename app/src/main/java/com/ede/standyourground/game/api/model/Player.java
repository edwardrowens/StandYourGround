package com.ede.standyourground.game.api.model;

import com.ede.standyourground.game.api.event.listener.CoinBalanceChangeListener;
import com.ede.standyourground.game.api.event.listener.IncomeAccruedListener;
import com.ede.standyourground.game.api.event.observer.CoinBalanceChangeObserver;

import java.util.UUID;

/**
 *
 */

public class Player implements CoinBalanceChangeObserver {

    private static final int BASE_INCOME = 15;

    private UUID id;
    private int coins;
    private long lastResourceAccrual;
    private int income;
    private int bankNeutralCampCount;
    private int medicNeutralCampCount;

    public Player(int coins) {
        this.coins = coins;
        this.income = BASE_INCOME;
    }

    // Listeners
    private CoinBalanceChangeListener coinBalanceChangeListener;
    private IncomeAccruedListener incomeAccruedListener;

    public int getCoins() {
        return coins;
    }

    public void updateCoins(int updateAmount) {
        int oldBalance = coins;
        this.coins += updateAmount;
        coinBalanceChangeListener.onCoinBalanceChange(oldBalance, this.coins);
    }

    public long getLastResourceAccrual() {
        return lastResourceAccrual;
    }

    public void setLastResourceAccrual(long lastResourceAccrual) {
        this.lastResourceAccrual = lastResourceAccrual;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public void registerCoinBalanceChangeListener(CoinBalanceChangeListener coinBalanceChangeListener) {
        this.coinBalanceChangeListener = coinBalanceChangeListener;
    }

    public int getIncome() {
        return income;
    }

    public void updateIncome(int updateAmount) {
        this.income += updateAmount;
    }

    public int incrementBankNeutralCampCount() {
        return ++bankNeutralCampCount;
    }

    public int decrementBankNeutralCampCount() {
        return --bankNeutralCampCount;
    }

    public int incrementMedicNeutralCampCount() {
        return ++medicNeutralCampCount;
    }

    public int decrementMedicNeutralCampCount() {
        return --medicNeutralCampCount;
    }

    public int getBankNeutralCampCount() {
        return bankNeutralCampCount;
    }

    public int getMedicNeutralCampCount() {
        return medicNeutralCampCount;
    }
}
