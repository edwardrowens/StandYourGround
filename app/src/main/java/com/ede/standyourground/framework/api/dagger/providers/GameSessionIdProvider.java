package com.ede.standyourground.framework.api.dagger.providers;

import java.util.UUID;

public class GameSessionIdProvider {
    private UUID gameSessionId;

    public UUID getGameSessionId() {
        return gameSessionId;
    }

    public void setGameSessionId(UUID gameSessionId) {
        this.gameSessionId = gameSessionId;
    }
}
