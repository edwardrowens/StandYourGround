package com.ede.standyourground.game.model.api;

import com.ede.standyourground.game.model.Unit;

public interface HealthChangeListener {
    void onHealthChange(Unit unit);
}
