package com.ede.standyourground.networking.exchange.request.handler.impl;

import com.ede.standyourground.framework.WorldManager;
import com.ede.standyourground.game.model.FootSoldier;
import com.ede.standyourground.game.model.Unit;
import com.ede.standyourground.networking.exchange.request.api.Request;
import com.ede.standyourground.networking.exchange.request.handler.api.RequestHandler;
import com.ede.standyourground.networking.exchange.request.impl.CreateUnitRequest;

/**
 * Created by Eddie on 2/15/2017.
 */

public class CreateUnitRequestHandler implements RequestHandler {
    @Override
    public <T extends Request> void handle(T request) {
        CreateUnitRequest createUnitRequest = (CreateUnitRequest) request;
        switch(createUnitRequest.getUnit()) {
            case FOOT_SOLDIER:
                Unit unit = new FootSoldier(createUnitRequest.getWaypoints(), createUnitRequest.getPosition());
                unit.setCreatedTime(createUnitRequest.getTimestamp());
                WorldManager.getInstance().addUnit(unit);
        }
    }
}
