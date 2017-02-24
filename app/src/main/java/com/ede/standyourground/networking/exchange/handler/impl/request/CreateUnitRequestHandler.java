package com.ede.standyourground.networking.exchange.handler.impl.request;

import com.ede.standyourground.framework.WorldManager;
import com.ede.standyourground.game.model.FootSoldier;
import com.ede.standyourground.game.model.Unit;
import com.ede.standyourground.networking.exchange.handler.api.ExchangeHandler;
import com.ede.standyourground.networking.exchange.request.impl.CreateUnitRequest;
import com.ede.standyourground.networking.exchange.response.impl.OkResponse;
import com.ede.standyourground.networking.framework.NetworkingManager;

public class CreateUnitRequestHandler implements ExchangeHandler {
    @Override
    public <T> void handle(T exchange) {
        CreateUnitRequest createUnitRequest = (CreateUnitRequest) exchange;
        switch (createUnitRequest.getUnit()) {
            case FOOT_SOLDIER:
                Unit unit = new FootSoldier(createUnitRequest.getWaypoints(), createUnitRequest.getPosition());
                unit.setCreatedTime(createUnitRequest.getTimestamp());
                WorldManager.getInstance().addUnit(unit, createUnitRequest.getGameSessionId());
        }

        OkResponse okResponse = new OkResponse();
        okResponse.setGameSessionId(createUnitRequest.getGameSessionId());
        NetworkingManager.getInstance().sendExchange(okResponse);
    }
}
