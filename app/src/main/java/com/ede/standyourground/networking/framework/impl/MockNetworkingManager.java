package com.ede.standyourground.networking.framework.impl;

import com.ede.standyourground.framework.Callback;
import com.ede.standyourground.framework.Logger;
import com.ede.standyourground.networking.exchange.api.Exchange;
import com.ede.standyourground.networking.framework.api.NetworkingManager;

import javax.inject.Inject;

public class MockNetworkingManager implements NetworkingManager {

    private static final Logger logger = new Logger(MockNetworkingManager.class);

    @Inject
    MockNetworkingManager() {

    }

    @Override
    public void connect(String gameSessionId, Callback callback) {
        logger.i("Connect called");
        callback.onSuccess();
    }

    @Override
    public void sendExchange(Exchange exchange) {
        logger.i("Sending %s exchange", exchange.getType());

    }

    @Override
    public void closeConnection() {
        logger.i("Closing connection");
    }
}
