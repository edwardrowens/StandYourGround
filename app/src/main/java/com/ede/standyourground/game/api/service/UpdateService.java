package com.ede.standyourground.game.api.service;


import com.ede.standyourground.game.api.model.Unit;

import java.util.List;

public interface UpdateService {

    void determineVisibility(Unit unit);
    void determinePosition(Unit unit);
    void processCombat(List<Unit> units);
    void calculateResourceAccrual();
}
