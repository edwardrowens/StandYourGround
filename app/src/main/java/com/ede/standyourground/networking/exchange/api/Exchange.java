package com.ede.standyourground.networking.exchange.api;

import java.util.UUID;

/**
 * Created by Eddie on 2/14/2017.
 */

public interface Exchange {
    long getTimestamp();
    void setTimestamp(long timestamp);
    UUID getId();
}
