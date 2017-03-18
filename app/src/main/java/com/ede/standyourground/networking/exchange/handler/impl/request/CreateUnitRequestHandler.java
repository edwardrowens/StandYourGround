package com.ede.standyourground.networking.exchange.handler.impl.request;

import com.ede.standyourground.game.framework.management.impl.UnitServiceImpl;
import com.ede.standyourground.networking.exchange.handler.api.ExchangeHandler;
import com.ede.standyourground.networking.exchange.request.CreateUnitRequest;
import com.ede.standyourground.networking.exchange.response.OkResponse;
import com.ede.standyourground.networking.framework.api.NetworkingManager;

import javax.inject.Inject;

import dagger.Lazy;

public class CreateUnitRequestHandler implements ExchangeHandler {

    private final Lazy<UnitServiceImpl> worldManager;
    private final Lazy<NetworkingManager> networkingManager;

    @Inject
    CreateUnitRequestHandler(Lazy<UnitServiceImpl> worldManager,
                             Lazy<NetworkingManager> networkingManager) {
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
