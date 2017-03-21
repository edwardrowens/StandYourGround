package com.ede.standyourground.networking.impl.exchange.handler.response;

import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.networking.api.exchange.handler.ExchangeHandler;

public class OkResponseHandler implements ExchangeHandler {
    private static Logger logger = new Logger(OkResponseHandler.class);

    @Override
    public <T> void handle(T exchange) {
        logger.i("OK response received.");
    }
}
