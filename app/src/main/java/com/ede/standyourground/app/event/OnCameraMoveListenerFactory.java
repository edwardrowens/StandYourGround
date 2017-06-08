package com.ede.standyourground.app.event;

import com.ede.standyourground.app.ui.api.service.UnitChoicesMenuService;
import com.ede.standyourground.app.ui.api.service.UnitGroupComponentService;
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
    private final Lazy<UnitGroupComponentService> unitGroupComponentService;


    @Inject
    OnCameraMoveListenerFactory(Lazy<UnitChoicesMenuService> unitChoicesMenuService,
                                Lazy<UnitGroupComponentService> unitGroupComponentService) {
        this.unitChoicesMenuService = unitChoicesMenuService;
        this.unitGroupComponentService = unitGroupComponentService;
    }

    public GoogleMap.OnCameraMoveListener createOnCameraMoveListener(final UnitGroupComponent unitGroupComponent,
                                                                     final NeutralCampListingComponent neutralCampListingComponent,
                                                                     final UnitChoicesMenuComponent unitChoicesMenuComponent) {
        return new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                unitGroupComponentService.get().clear(unitGroupComponent);
                neutralCampListingComponent.clear();
                unitChoicesMenuService.get().clear(unitChoicesMenuComponent);
            }
        };
    }
}
