package com.ede.standyourground.app.ui.api.service;

import com.ede.standyourground.app.ui.impl.component.UnitChoicesMenuComponent;
import com.ede.standyourground.game.api.model.Units;

/**
 *
 */

public interface UnitChoicesMenuService {
    void onUnitSelected(UnitChoicesMenuComponent unitChoicesMenuComponent, Units units);
    void onRouteCancelled(UnitChoicesMenuComponent unitChoicesMenuComponent);
    void onConfirmRoute(UnitChoicesMenuComponent unitChoicesMenuComponent);
    void setVisibility(UnitChoicesMenuComponent unitChoicesMenuComponent, Units units, int visibility);
    void realign(UnitChoicesMenuComponent unitChoicesMenuComponent);
    void clear(UnitChoicesMenuComponent unitChoicesMenuComponent);
}
