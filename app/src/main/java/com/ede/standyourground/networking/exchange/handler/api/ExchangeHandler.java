package com.ede.standyourground.networking.exchange.handler.api;

/**
 * Created by Eddie on 2/16/2017.
 */

public interface ExchangeHandler {
    <T> void handle(T exchange);
}
