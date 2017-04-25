package com.ede.standyourground.game.impl.model;

import android.os.Handler;

import com.ede.standyourground.game.api.model.ArtificialOpponentDifficulty;
import com.ede.standyourground.game.api.model.Player;
import com.google.android.gms.maps.model.LatLng;

/**
 *
 */

public class ArtificialOpponent {

    private final Player player;
    private final Handler handler;
    private final ArtificialOpponentDifficulty artificialOpponentDifficulty;
    private final LatLng basePosition;
    private final LatLng opponentPosition;

    public ArtificialOpponent(Player player, Handler handler, ArtificialOpponentDifficulty artificialOpponentDifficulty, LatLng basePosition, LatLng opponentPosition) {
        this.player = player;
        this.handler = handler;
        this.artificialOpponentDifficulty = artificialOpponentDifficulty;
        this.basePosition = basePosition;
        this.opponentPosition = opponentPosition;
    }

    public Player getPlayer() {
        return player;
    }

    public Handler getHandler() {
        return handler;
    }

    public ArtificialOpponentDifficulty getArtificialOpponentDifficulty() {
        return artificialOpponentDifficulty;
    }

    public LatLng getBasePosition() {
        return basePosition;
    }

    public LatLng getOpponentPosition() {
        return opponentPosition;
    }
}
