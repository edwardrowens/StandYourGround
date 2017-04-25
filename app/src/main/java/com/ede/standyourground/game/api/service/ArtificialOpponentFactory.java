package com.ede.standyourground.game.api.service;

import android.os.Handler;
import android.os.HandlerThread;

import com.ede.standyourground.game.api.model.ArtificialOpponentDifficulty;
import com.ede.standyourground.game.api.model.Player;
import com.ede.standyourground.game.impl.model.ArtificialOpponent;
import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;

import javax.inject.Inject;

import dagger.Lazy;

/**
 *
 */

public class ArtificialOpponentFactory {

    private final Lazy<PlayerService> playerService;

    @Inject
    public ArtificialOpponentFactory(Lazy<PlayerService> playerService) {
        this.playerService = playerService;
    }

    public ArtificialOpponent createArtificialOpponent(ArtificialOpponentDifficulty artificialOpponentDifficulty, LatLng basePosition, LatLng opponentPosition) {
        HandlerThread handlerThread = new HandlerThread("AI");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());

        Player player = new Player(UUID.randomUUID(), false);
        playerService.get().addPlayer(player);

        return new ArtificialOpponent(player, handler, artificialOpponentDifficulty, basePosition, opponentPosition);
    }
}
