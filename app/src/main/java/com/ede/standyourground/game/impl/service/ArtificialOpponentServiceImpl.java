package com.ede.standyourground.game.impl.service;

import com.ede.standyourground.game.api.model.UnitType;
import com.ede.standyourground.game.api.service.ArtificialOpponentService;
import com.ede.standyourground.game.api.service.GameService;
import com.ede.standyourground.game.impl.model.ArtificialOpponent;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.Lazy;

/**
 *
 */

public class ArtificialOpponentServiceImpl implements ArtificialOpponentService {

    private final Lazy<GameService> gameService;

    @Inject
    public ArtificialOpponentServiceImpl(Lazy<GameService> gameService) {
        this.gameService = gameService;
    }

    @Override
    public void runArtificialOpponent(ArtificialOpponent artificialOpponent) {
        switch(artificialOpponent.getArtificialOpponentDifficulty()) {
            case EASY:
                runEasyArtificialOpponent(artificialOpponent);
                break;
            case MEDIUM:
                runMediumArtificialOpponent(artificialOpponent);
                break;
            case HARD:
                runHardArtificialOpponent(artificialOpponent);
                break;
        }
    }

    private void runEasyArtificialOpponent(final ArtificialOpponent artificialOpponent) {
        artificialOpponent.getHandler().post(new Runnable() {
            @Override
            public void run() {
                artificialOpponent.getHandler().postDelayed(this, 30000);
                gameService.get().createEntity(artificialOpponent.getPlayer().getId(), UnitType.FOOT_SOLDIER, artificialOpponent.getBasePosition(), artificialOpponent.getOpponentPosition(), new ArrayList<LatLng>());
            }
        });
    }

    private void runMediumArtificialOpponent(final ArtificialOpponent artificialOpponent) {
        artificialOpponent.getHandler().post(new Runnable() {
            @Override
            public void run() {
                artificialOpponent.getHandler().postDelayed(this, 30000);
                gameService.get().createEntity(artificialOpponent.getPlayer().getId(), UnitType.FOOT_SOLDIER, artificialOpponent.getBasePosition(), artificialOpponent.getOpponentPosition(), new ArrayList<LatLng>());
            }
        });
    }

    private void runHardArtificialOpponent(final ArtificialOpponent artificialOpponent) {
        artificialOpponent.getHandler().post(new Runnable() {
            @Override
            public void run() {
                artificialOpponent.getHandler().postDelayed(this, 30000);
                gameService.get().createEntity(artificialOpponent.getPlayer().getId(), UnitType.FOOT_SOLDIER, artificialOpponent.getBasePosition(), artificialOpponent.getOpponentPosition(), new ArrayList<LatLng>());
            }
        });
    }
}
