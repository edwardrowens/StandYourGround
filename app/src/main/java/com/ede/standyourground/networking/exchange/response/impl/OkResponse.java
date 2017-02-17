package com.ede.standyourground.networking.exchange.response.impl;

import com.ede.standyourground.networking.exchange.response.api.Response;

/**
 * Created by Eddie on 2/16/2017.
 */

public class OkResponse implements Response {

    private long timestamp;

    public OkResponse() {
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
