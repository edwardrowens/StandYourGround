package com.ede.standyourground.networking.framework;

import com.ede.standyourground.networking.exchange.request.api.Request;
import com.ede.standyourground.networking.exchange.request.handler.impl.CreateUnitRequestHandler;
import com.ede.standyourground.networking.exchange.request.impl.CreateUnitRequest;
import com.ede.standyourground.networking.exchange.response.api.Response;

/**
 * Created by Eddie on 2/15/2017.
 */

public class ExchangeHandlerUtil {
    public static void handleRequest(Request request) {
        if (request instanceof CreateUnitRequest) {
            new CreateUnitRequestHandler().handle(request);
        }
    }

    public static void handleResponse(Response response) {

    }
}
