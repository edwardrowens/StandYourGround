package com.ede.standyourground.networking.api;

import com.ede.standyourground.framework.api.transmit.Callback;
import com.ede.standyourground.networking.api.exchange.payload.Exchange;

public interface NetworkingHandler {

    void connect(final String gameSessionId, final Callback callback);
    void sendExchange(final Exchange exchange);
    void closeConnection();
}
