package com.ede.standyourground.app.ui.impl.service;

import android.view.View;

import com.ede.standyourground.app.ui.api.service.UnitGroupBlockHealthBarComponentService;
import com.ede.standyourground.app.ui.impl.component.UnitGroupBlockHealthBarComponent;
import com.ede.standyourground.game.api.service.UnitService;

import javax.inject.Inject;

import dagger.Lazy;

/**
 *
 */

public class UnitGroupBlockHealthBarComponentServiceImpl implements UnitGroupBlockHealthBarComponentService {

    private final Lazy<UnitService> unitService;

    @Inject
    public UnitGroupBlockHealthBarComponentServiceImpl(Lazy<UnitService> unitService) {
        this.unitService = unitService;
    }

    @Override
    public void clear(UnitGroupBlockHealthBarComponent unitGroupBlockHealthBarComponent) {
        unitService.get().removeOnDeathListener(unitGroupBlockHealthBarComponent.getOnDeathListenerHook());
        unitGroupBlockHealthBarComponent.getContainer().removeAllViews();
        unitGroupBlockHealthBarComponent.getContainer().setVisibility(View.GONE);
    }
}
