package com.ede.standyourground.networking.api.exchange.handler;

/**
 * Created by Eddie on 2/16/2017.
 */

public interface ExchangeHandler {
    <T> void handle(T exchange);
}
