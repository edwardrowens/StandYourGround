package com.ede.standyourground.networking.exchange.handler.impl.response;

import com.ede.standyourground.framework.Logger;
import com.ede.standyourground.networking.exchange.handler.api.ExchangeHandler;

/**
 * Created by Eddie on 2/16/2017.
 */

public class OkResponseHandler implements ExchangeHandler {
    private static Logger logger = new Logger(OkResponseHandler.class);

    @Override
    public <T> void handle(T exchange) {
        logger.i("OK response received.");
    }
}
