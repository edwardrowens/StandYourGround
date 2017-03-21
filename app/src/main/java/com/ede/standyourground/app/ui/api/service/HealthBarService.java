package com.ede.standyourground.app.ui.api.service;

import com.ede.standyourground.app.ui.impl.component.HealthBar;
import com.ede.standyourground.game.api.model.Unit;

/**
 *
 */

public interface HealthBarService {
    void setHealthBarPosition(Unit unit, HealthBar healthBar);
}
