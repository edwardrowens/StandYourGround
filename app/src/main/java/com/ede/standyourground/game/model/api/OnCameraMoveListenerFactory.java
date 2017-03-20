package com.ede.standyourground.game.model.api;

import com.ede.standyourground.app.ui.HealthBarComponent;
import com.ede.standyourground.app.ui.HealthBarService;
import com.ede.standyourground.app.ui.UnitGroupComponent;
import com.ede.standyourground.game.framework.management.api.UnitService;
import com.ede.standyourground.game.model.Unit;
import com.google.android.gms.maps.GoogleMap;

import javax.inject.Inject;

import dagger.Lazy;

public class OnCameraMoveListenerFactory {

    private final Lazy<UnitService> unitService;
    private final Lazy<HealthBarService> healthBarService;

    @Inject
    OnCameraMoveListenerFactory(Lazy<UnitService> unitService,
                                Lazy<HealthBarService> healthBarService) {
        this.unitService = unitService;
        this.healthBarService = healthBarService;
    }

    public GoogleMap.OnCameraMoveListener createOnCameraMoveListener(final UnitGroupComponent unitGroupComponent, final HealthBarComponent healthBarComponent) {
        return new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                unitGroupComponent.clear();

                for (Unit unit : unitService.get().getUnits()) {
                    if (healthBarComponent.containsHealthBar(unit.getId())) {
                        healthBarService.get().setHealthBarPosition(unit, healthBarComponent.getElement(unit.getId()));
                    }
                }
            }
        };
    }
}
