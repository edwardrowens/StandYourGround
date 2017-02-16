package com.ede.standyourground.networking.exchange.request.handler.api;

import com.ede.standyourground.networking.exchange.request.api.Request;

/**
 * Created by Eddie on 2/15/2017.
 */

public interface RequestHandler {
    <T extends Request> void handle(T request);
}
