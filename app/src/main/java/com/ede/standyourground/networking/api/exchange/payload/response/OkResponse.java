package com.ede.standyourground.networking.api.exchange.payload.response;

import com.ede.standyourground.networking.api.exchange.payload.Exchange;

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
