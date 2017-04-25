package com.ede.standyourground.game.api.model;

import com.ede.standyourground.game.api.event.listener.CoinBalanceChangeListener;
import com.ede.standyourground.game.api.event.observer.CoinBalanceChangeObserver;

import java.util.UUID;

/**
 *
 */
public class Player implements CoinBalanceChangeObserver {

    private static final int BASE_INCOME = 15;
    private static final int STARTING_COINS = 200;

    private final UUID id;
    private final boolean mainPlayer;

    private long lastResourceAccrual;
    private int coins;
    private int income;
    private int bankNeutralCampCount;
    private int medicNeutralCampCount;

    public Player(UUID id, boolean mainPlayer) {
        this.id = id;
        this.mainPlayer = mainPlayer;
        this.lastResourceAccrual = System.currentTimeMillis();
        this.coins = STARTING_COINS;
        this.income = BASE_INCOME;
        this.bankNeutralCampCount = 0;
        this.medicNeutralCampCount = 0;
    }

    // Listeners
    private CoinBalanceChangeListener coinBalanceChangeListener;

    public int getCoins() {
        return coins;
    }

    public void updateCoins(int updateAmount) {
        int oldBalance = coins;
        this.coins += updateAmount;
        coinBalanceChangeListener.onCoinBalanceChange(this, oldBalance, this.coins);
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

    public boolean isMainPlayer() {
        return mainPlayer;
    }
}
