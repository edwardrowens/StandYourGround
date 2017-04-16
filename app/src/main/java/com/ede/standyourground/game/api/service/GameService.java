package com.ede.standyourground.game.api.service;

import com.ede.standyourground.game.api.event.observer.CoinBalanceChangeObserver;
import com.ede.standyourground.game.api.event.observer.GameEndObserver;
import com.ede.standyourground.game.api.model.WorldGrid;
import com.google.android.gms.maps.model.LatLng;

/**
 *
 */

public interface GameService extends GameEndObserver, CoinBalanceChangeObserver {
    void startGame(LatLng playerLocation, LatLng opponentLocation);
    void stopGame();
    WorldGrid getWorldGrid();
}
