package com.ede.standyourground.networking.impl;

import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.framework.api.service.DrawRouteService;
import com.ede.standyourground.framework.api.transmit.Callback;
import com.ede.standyourground.networking.api.NetworkingHandler;
import com.ede.standyourground.networking.api.exchange.payload.Exchange;

import javax.inject.Inject;

import dagger.Lazy;

public class MockNetworkingHandler implements NetworkingHandler {

    private static final Logger logger = new Logger(MockNetworkingHandler.class);
    private final Lazy<DrawRouteService> drawRouteService;

    @Inject
    MockNetworkingHandler(Lazy<DrawRouteService> drawRouteService) {
        this.drawRouteService = drawRouteService;
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
