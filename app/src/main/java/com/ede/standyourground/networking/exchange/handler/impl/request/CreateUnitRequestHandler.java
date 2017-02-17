package com.ede.standyourground.networking.exchange.handler.impl.request;

import com.ede.standyourground.framework.WorldManager;
import com.ede.standyourground.game.model.FootSoldier;
import com.ede.standyourground.game.model.Unit;
import com.ede.standyourground.networking.exchange.handler.api.ExchangeHandler;
import com.ede.standyourground.networking.exchange.request.impl.CreateUnitRequest;

/**
 * Created by Eddie on 2/15/2017.
 */

public class CreateUnitRequestHandler implements ExchangeHandler {
    @Override
    public <T> void handle(T exchange) {
        CreateUnitRequest createUnitRequest = (CreateUnitRequest) exchange;
        switch (createUnitRequest.getUnit()) {
            case FOOT_SOLDIER:
                Unit unit = new FootSoldier(createUnitRequest.getWaypoints(), createUnitRequest.getPosition());
                unit.setCreatedTime(createUnitRequest.getTimestamp());
                WorldManager.getInstance().addUnit(unit);
        }
    }
}
