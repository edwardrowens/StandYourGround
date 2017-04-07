package com.ede.standyourground.app.event;

import com.ede.standyourground.app.ui.api.service.HealthBarService;
import com.ede.standyourground.app.ui.impl.component.HealthBarComponent;
import com.ede.standyourground.app.ui.impl.component.NeutralCampListingComponent;
import com.ede.standyourground.app.ui.impl.component.UnitChoicesComponent;
import com.ede.standyourground.app.ui.impl.component.UnitGroupComponent;
import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.game.api.model.Unit;
import com.ede.standyourground.game.api.service.UnitService;
import com.google.android.gms.maps.GoogleMap;

import javax.inject.Inject;

import dagger.Lazy;

public class OnCameraMoveListenerFactory {

    private static final Logger logger = new Logger(OnCameraMoveListenerFactory.class);

    private final Lazy<UnitService> unitService;
    private final Lazy<HealthBarService> healthBarService;

    @Inject
    OnCameraMoveListenerFactory(Lazy<UnitService> unitService,
                                Lazy<HealthBarService> healthBarService) {
        this.unitService = unitService;
        this.healthBarService = healthBarService;
    }

    public GoogleMap.OnCameraMoveListener createOnCameraMoveListener(final UnitGroupComponent unitGroupComponent,
                                                                     final HealthBarComponent healthBarComponent,
                                                                     final NeutralCampListingComponent neutralCampListingComponent,
                                                                     final UnitChoicesComponent unitChoicesComponent) {
        return new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                unitGroupComponent.clear();
                neutralCampListingComponent.clear();
                unitChoicesComponent.clear();

                long t = System.currentTimeMillis();
                for (Unit unit : unitService.get().getUnits()) {
                    if (healthBarComponent.containsHealthBar(unit.getId())) {
                        healthBarService.get().setHealthBarPosition(unit, healthBarComponent.getElement(unit.getId()));
                    }
                }
                logger.e("HEALTH BARS: %d", System.currentTimeMillis() - t);
            }
        };
    }
}
