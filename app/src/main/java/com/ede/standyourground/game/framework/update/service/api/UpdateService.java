package com.ede.standyourground.game.framework.update.service.api;


import com.ede.standyourground.game.model.Unit;

import java.util.List;

public interface UpdateService {

    void determineVisibility(Unit unit);
    void determinePosition(Unit unit);
    void processCombat(List<Unit> units);
}
