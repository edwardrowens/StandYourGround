package com.ede.standyourground.app.ui.api.component;

import android.app.Activity;
import android.view.ViewGroup;

import com.ede.standyourground.app.ui.impl.component.UnitChoicesComponent;
import com.ede.standyourground.framework.api.service.GraphicService;

import javax.inject.Inject;

import dagger.Lazy;

/**
 *
 */

public class UnitChoicesComponentFactory {

    @Inject
    UnitChoicesComponentFactory(Lazy<GraphicService> graphicService) {
    }

    public UnitChoicesComponent createUnitChoicesComponent(Activity activity, ViewGroup parent) {
        return new UnitChoicesComponent(activity, parent);
    }
}
