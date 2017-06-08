package com.ede.standyourground.app.ui.impl.service;

import android.view.View;

import com.ede.standyourground.R;
import com.ede.standyourground.app.ui.api.service.UnitGroupBlockCountComponentService;
import com.ede.standyourground.app.ui.impl.component.UnitGroupBlockCount;
import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.game.api.service.UnitService;

import javax.inject.Inject;

import dagger.Lazy;

/**
 *
 */

public class UnitGroupBlockCountComponentServiceImpl implements UnitGroupBlockCountComponentService {

    private static final Logger logger = new Logger(UnitGroupBlockCountComponentServiceImpl.class);

    private final Lazy<UnitService> unitService;

    @Inject
    public UnitGroupBlockCountComponentServiceImpl(Lazy<UnitService> unitService) {
        this.unitService = unitService;
    }

    @Override
    public void setCount(UnitGroupBlockCount unitGroupBlockCount, int count) {
        unitGroupBlockCount.getCountContainer().setText(unitGroupBlockCount.getActivity().getResources().getString(R.string.unitGroupCountText, count));
    }

    @Override
    public void clear(UnitGroupBlockCount unitGroupBlockCount) {
        unitService.get().removeOnDeathListener(unitGroupBlockCount.getOnDeathListenerHook());
        unitGroupBlockCount.getContainer().removeAllViews();
        unitGroupBlockCount.getContainer().setVisibility(View.GONE);
    }
}
