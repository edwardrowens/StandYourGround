package com.ede.standyourground.app.event;

import com.ede.standyourground.app.ui.api.service.HealthBarService;
import com.ede.standyourground.app.ui.impl.component.HealthBarComponent;
import com.ede.standyourground.app.ui.impl.component.UnitGroupComponent;
import com.ede.standyourground.game.api.model.Unit;
import com.ede.standyourground.game.api.service.UnitService;
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