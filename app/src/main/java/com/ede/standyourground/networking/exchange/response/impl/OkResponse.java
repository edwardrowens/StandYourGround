package com.ede.standyourground.networking.exchange.response.impl;

import com.ede.standyourground.networking.exchange.response.api.Response;

import java.util.UUID;

public class OkResponse implements Response {

    private long timestamp;
    private final UUID id = UUID.randomUUID();
    private UUID gameSessionId;

    public OkResponse() {
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public UUID getGameSessionId() {
        return gameSessionId;
    }

    @Override
    public void setGameSessionId(UUID gameSessionId) {
        this.gameSessionId = gameSessionId;
    }
}
