package com.ede.standyourground.networking.framework;

import com.ede.standyourground.networking.exchange.api.Exchange;
import com.ede.standyourground.networking.exchange.handler.impl.request.CreateUnitRequestHandler;
import com.ede.standyourground.networking.exchange.handler.impl.response.OkResponseHandler;
import com.ede.standyourground.networking.exchange.request.api.Request;
import com.ede.standyourground.networking.exchange.request.impl.CreateUnitRequest;
import com.ede.standyourground.networking.exchange.response.api.Response;
import com.ede.standyourground.networking.exchange.response.impl.OkResponse;

public class ExchangeHandlerUtil {
    public static void handleRequest(Request request) {
        if (request instanceof CreateUnitRequest) {
            new CreateUnitRequestHandler().handle(request);
        }
    }

    public static void handleResponse(Response response) {
        if (response instanceof OkResponse) {
            new OkResponseHandler().handle(response);
        }
    }

    public static void handleExchange(Exchange exchange) {
        if (exchange instanceof  Request) {
            handleRequest((Request) exchange);
        } else {
            handleResponse((Response) exchange);
        }
    }
}
