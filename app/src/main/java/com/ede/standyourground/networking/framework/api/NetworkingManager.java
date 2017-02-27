package com.ede.standyourground.networking.framework.api;

import com.ede.standyourground.framework.Callback;
import com.ede.standyourground.networking.exchange.api.Exchange;

public interface NetworkingManager {

    void connect(final String gameSessionId, final Callback callback);
    void sendExchange(final Exchange exchange);
    void closeConnection();
}
