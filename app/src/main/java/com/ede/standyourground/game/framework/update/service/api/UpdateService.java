package com.ede.standyourground.game.framework.update.service.api;


import com.ede.standyourground.game.model.Unit;

public interface UpdateService {

    void determineVisibility(Unit unit);
    void determinePosition(Unit unit);
}
