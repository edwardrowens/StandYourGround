package com.ede.standyourground.app.event;

import com.ede.standyourground.app.ui.api.service.UnitChoicesMenuService;
import com.ede.standyourground.app.ui.impl.component.NeutralCampListingComponent;
import com.ede.standyourground.app.ui.impl.component.UnitChoicesMenuComponent;
import com.ede.standyourground.app.ui.impl.component.UnitGroupComponent;
import com.ede.standyourground.framework.api.Logger;
import com.google.android.gms.maps.GoogleMap;

import javax.inject.Inject;

import dagger.Lazy;

public class OnCameraMoveListenerFactory {

    private static final Logger logger = new Logger(OnCameraMoveListenerFactory.class);
    private final Lazy<UnitChoicesMenuService> unitChoicesMenuService;


    @Inject
    OnCameraMoveListenerFactory(Lazy<UnitChoicesMenuService> unitChoicesMenuService) {
        this.unitChoicesMenuService = unitChoicesMenuService;
    }

    public GoogleMap.OnCameraMoveListener createOnCameraMoveListener(final UnitGroupComponent unitGroupComponent,
//                                                                     final HealthBarComponent healthBarComponent,
                                                                     final NeutralCampListingComponent neutralCampListingComponent,
                                                                     final UnitChoicesMenuComponent unitChoicesMenuComponent) {
        return new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                unitGroupComponent.clear();
                neutralCampListingComponent.clear();
                unitChoicesMenuService.get().clear(unitChoicesMenuComponent);

//                for (Unit unit : unitService.get().getUnits()) {
//                    if (healthBarComponent.containsHealthBar(unit.getId())) {
//                        healthBarService.get().setHealthBarPosition(unit, healthBarComponent.getElement(unit.getId()));
//                    }
//                }
            }
        };
    }
}
