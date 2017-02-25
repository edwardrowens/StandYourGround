package com.ede.standyourground.networking.exchange.response.impl;

import com.ede.standyourground.networking.exchange.api.Exchange;

public class OkResponse extends Exchange {

    private final String type = OkResponse.class.getSimpleName();

    @Override
    public String getType() {
        return type;
    }
}
