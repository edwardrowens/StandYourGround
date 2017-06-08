package com.ede.standyourground.app.ui.api.service;

import com.ede.standyourground.app.ui.impl.component.UnitGroupBlockCount;
import com.ede.standyourground.app.ui.impl.component.UnitGroupBlockHealthBarComponent;
import com.ede.standyourground.app.ui.impl.component.UnitGroupComponent;
import com.google.android.gms.maps.model.LatLng;

/**
 *
 */

public interface UnitGroupComponentService {
    void realign(UnitGroupComponent unitGroupComponent, LatLng latLng);
    void addUnitGroupBlock(UnitGroupComponent unitGroupComponent, UnitGroupBlockCount unitGroupBlockCount);
    void addUnitGroupBlock(UnitGroupComponent unitGroupComponent, UnitGroupBlockHealthBarComponent unitGroupBlockHealthBarComponent);
    void addUnitGroupBlock(UnitGroupComponent unitGroupComponent, UnitGroupBlockHealthBarComponent unitGroupBlockHealthBarComponent, int index);
    void clear(UnitGroupComponent unitGroupComponent);
    void setVisibility(UnitGroupComponent unitGroupComponent, int visibility);
}
