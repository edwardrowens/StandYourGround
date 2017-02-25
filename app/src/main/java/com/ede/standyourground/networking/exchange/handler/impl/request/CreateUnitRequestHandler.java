package com.ede.standyourground.networking.exchange.handler.impl.request;

import com.ede.standyourground.game.framework.management.impl.WorldManager;
import com.ede.standyourground.networking.exchange.handler.api.ExchangeHandler;
import com.ede.standyourground.networking.exchange.request.impl.CreateUnitRequest;
import com.ede.standyourground.networking.exchange.response.impl.OkResponse;
import com.ede.standyourground.networking.framework.NetworkingManager;

public class CreateUnitRequestHandler implements ExchangeHandler {
    @Override
    public <T> void handle(T exchange) {
        CreateUnitRequest createUnitRequest = (CreateUnitRequest) exchange;
        WorldManager.getInstance().createUnit(createUnitRequest.getWaypoints(), createUnitRequest.getPosition(), createUnitRequest.getUnit(), false);

        OkResponse okResponse = new OkResponse(createUnitRequest);
        NetworkingManager.getInstance().sendExchange(okResponse);
    }
}
