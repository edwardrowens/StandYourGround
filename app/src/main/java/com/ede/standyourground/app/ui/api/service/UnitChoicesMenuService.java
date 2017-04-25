package com.ede.standyourground.app.ui.api.service;

import com.ede.standyourground.app.ui.impl.component.UnitChoicesMenuComponent;
import com.ede.standyourground.game.api.model.UnitType;

/**
 *
 */

public interface UnitChoicesMenuService {
    void onUnitSelected(UnitChoicesMenuComponent unitChoicesMenuComponent, UnitType unitType);
    void onRouteCancelled(UnitChoicesMenuComponent unitChoicesMenuComponent);
    void onConfirmRoute(UnitChoicesMenuComponent unitChoicesMenuComponent);
    void setVisibility(UnitChoicesMenuComponent unitChoicesMenuComponent, UnitType unitType, int visibility);
    void realign(UnitChoicesMenuComponent unitChoicesMenuComponent);
    void clear(UnitChoicesMenuComponent unitChoicesMenuComponent);
    void checkUnitsAvailableForPurchase(UnitChoicesMenuComponent unitChoicesMenuComponent);
}
