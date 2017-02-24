package com.ede.standyourground.networking.exchange.api;

import java.util.UUID;

public interface Exchange {
    long getTimestamp();
    void setTimestamp(long timestamp);
    UUID getId();
    UUID getGameSessionId();
    void setGameSessionId(UUID gameSessionId);
}
