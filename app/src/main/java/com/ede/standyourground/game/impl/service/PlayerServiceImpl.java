package com.ede.standyourground.game.impl.service;

import com.ede.standyourground.game.api.event.listener.CoinBalanceChangeListener;
import com.ede.standyourground.game.api.event.listener.IncomeAccruedListener;
import com.ede.standyourground.game.api.model.Player;
import com.ede.standyourground.game.api.service.PlayerService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 *
 */
@Singleton
public class PlayerServiceImpl implements PlayerService {

    private final Map<UUID, Player> players = new HashMap<>();
    private UUID mainPlayerId;

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
            public void onCoinBalanceChange(Player player, int oldBalance, int newBalance) {
                for (CoinBalanceChangeListener coinBalanceChangeListener : coinBalanceChangeListeners) {
                    coinBalanceChangeListener.onCoinBalanceChange(player, oldBalance, newBalance);
                }
            }
        });
        players.put(player.getId(), player);
        if (player.isMainPlayer()) {
            mainPlayerId = player.getId();
        }
    }

    @Override
    public void makePurchase(UUID playerId, int costOfItem) {
        if (checkFunds(playerId, costOfItem)) {
            players.get(playerId).updateCoins(-costOfItem);
        }
    }

    @Override
    public void updateIncome(UUID playerId, int updateAmount) {
        players.get(playerId).updateIncome(updateAmount);
    }

    @Override
    public void accrueIncome() {
        for (Map.Entry<UUID, Player> entry : players.entrySet()) {
            entry.getValue().setLastResourceAccrual(System.currentTimeMillis());
            entry.getValue().updateCoins(entry.getValue().getIncome());
        }
        for (IncomeAccruedListener incomeAccruedListener : incomeAccruedListeners) {
            incomeAccruedListener.onIncomeAccrued();
        }
    }

    @Override
    public long getLastResourceAccrual(UUID playerId) {
        return players.get(playerId).getLastResourceAccrual();
    }

    @Override
    public boolean checkFunds(UUID playerId, int amountToCheck) {
        return players.get(playerId).getCoins() >= amountToCheck;
    }

    @Override
    public int getCurrentBalance(UUID playerId) {
        return players.get(playerId).getCoins();
    }

    @Override
    public int getMedicNeutralCampCount(UUID playerId) {
        return players.get(playerId).getMedicNeutralCampCount();
    }

    @Override
    public int getBankNeutralCampCount(UUID playerId) {
        return players.get(playerId).getBankNeutralCampCount();
    }

    @Override
    public UUID getMainPlayerId() {
        return mainPlayerId;
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
