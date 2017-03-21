package com.ede.standyourground.networking.impl.exchange.handler.request;

import com.ede.standyourground.game.impl.service.UnitServiceImpl;
import com.ede.standyourground.networking.api.NetworkingHandler;
import com.ede.standyourground.networking.api.exchange.handler.ExchangeHandler;
import com.ede.standyourground.networking.api.exchange.payload.request.CreateUnitRequest;
import com.ede.standyourground.networking.api.exchange.payload.response.OkResponse;

import javax.inject.Inject;

import dagger.Lazy;

public class CreateUnitRequestHandler implements ExchangeHandler {

    private final Lazy<UnitServiceImpl> worldManager;
    private final Lazy<NetworkingHandler> networkingManager;

    @Inject
    CreateUnitRequestHandler(Lazy<UnitServiceImpl> worldManager,
                             Lazy<NetworkingHandler> networkingManager) {
        this.worldManager = worldManager;
        this.networkingManager = networkingManager;
    }

    @Override
    public <T> void handle(T exchange) {
        CreateUnitRequest createUnitRequest = (CreateUnitRequest) exchange;
        worldManager.get().createEnemyUnit(createUnitRequest.getWaypoints(), createUnitRequest.getPosition(), createUnitRequest.getUnit());

        OkResponse okResponse = new OkResponse(createUnitRequest);
        networkingManager.get().sendExchange(okResponse);
    }
}
