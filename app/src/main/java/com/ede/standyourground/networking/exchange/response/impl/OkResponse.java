package com.ede.standyourground.networking.exchange.response.impl;

import com.ede.standyourground.networking.exchange.api.Exchange;

public class OkResponse extends Exchange {

    private final String type = OkResponse.class.getSimpleName();

    public OkResponse() {
        super();
    }

    public OkResponse(Exchange exchange) {
        this.gameSessionId = exchange.getGameSessionId();
        this.id = exchange.getId();
    }

    @Override
    public String getType() {
        return type;
    }
}
