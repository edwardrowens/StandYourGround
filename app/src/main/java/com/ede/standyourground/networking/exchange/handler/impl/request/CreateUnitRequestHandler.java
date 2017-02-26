package com.ede.standyourground.networking.exchange.handler.impl.request;

import com.ede.standyourground.game.framework.management.impl.WorldManager;
import com.ede.standyourground.networking.exchange.handler.api.ExchangeHandler;
import com.ede.standyourground.networking.exchange.request.impl.CreateUnitRequest;
import com.ede.standyourground.networking.exchange.response.impl.OkResponse;
import com.ede.standyourground.networking.framework.NetworkingManager;

import javax.inject.Inject;

import dagger.Lazy;

public class CreateUnitRequestHandler implements ExchangeHandler {

    private final Lazy<WorldManager> worldManager;
    private final Lazy<NetworkingManager> networkingManager;

    @Inject
    CreateUnitRequestHandler(Lazy<WorldManager> worldManager,
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
