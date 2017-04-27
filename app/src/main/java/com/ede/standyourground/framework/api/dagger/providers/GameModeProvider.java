package com.ede.standyourground.framework.api.dagger.providers;

import com.ede.standyourground.game.api.model.GameMode;

/**
 *
 */

public class GameModeProvider {
    private GameMode gameMode;

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }
}
