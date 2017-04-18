package com.ede.standyourground.game.api.service;


import com.ede.standyourground.game.api.model.Unit;

public interface UpdateService {
    void determineVisibility(Unit unit);
    void determinePosition(Unit unit);
    Unit processCombat(Unit unit);
    void calculateResourceAccrual();
}
