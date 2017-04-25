package com.ede.standyourground.game.api.service;

import com.ede.standyourground.game.api.event.observer.CoinBalanceChangeObserver;
import com.ede.standyourground.game.api.event.observer.GameEndObserver;
import com.ede.standyourground.game.api.model.GameMode;
import com.ede.standyourground.game.api.model.UnitType;
import com.ede.standyourground.game.api.model.WorldGrid;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.UUID;

/**
 *
 */

public interface GameService extends GameEndObserver, CoinBalanceChangeObserver {
    void startGame(GameMode gameMode, LatLng playerLocation, LatLng opponentLocation);
    void stopGame();
    void createEntity(UUID playerId, UnitType type, LatLng startPosition, LatLng endPosition, List<LatLng> intermediaryPositions);
    WorldGrid getWorldGrid();
}
