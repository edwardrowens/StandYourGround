package com.ede.standyourground.networking.exchange.api;

import java.util.UUID;

public abstract class Exchange {

    private long timestamp;
    private UUID id;
    private UUID gameSessionId;

    public abstract String getType();

    public Exchange() {
        this.id = UUID.randomUUID();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public UUID getId() {
        return id;
    }

    public UUID getGameSessionId() {
        return gameSessionId;
    }

    public void setGameSessionId(UUID gameSessionId) {
        this.gameSessionId = gameSessionId;
    }
}
