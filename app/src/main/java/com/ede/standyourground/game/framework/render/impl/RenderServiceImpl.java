package com.ede.standyourground.game.framework.render.impl;

import android.graphics.Point;

import com.ede.standyourground.app.activity.MapsActivity;
import com.ede.standyourground.app.ui.Component;
import com.ede.standyourground.app.ui.HealthBar;
import com.ede.standyourground.app.ui.HealthBarComponent;
import com.ede.standyourground.framework.api.MathService;
import com.ede.standyourground.framework.dagger.providers.GoogleMapProvider;
import com.ede.standyourground.game.framework.render.api.RenderService;
import com.ede.standyourground.game.model.Unit;
import com.google.maps.android.SphericalUtil;

import javax.inject.Inject;

import dagger.Lazy;

public class RenderServiceImpl implements RenderService {

    private final Lazy<GoogleMapProvider> googleMapProvider;
    private final Lazy<MathService> mathService;

    @Inject
    RenderServiceImpl(Lazy<GoogleMapProvider> googleMapProvider,
                      Lazy<MathService> mathService) {
        this.googleMapProvider = googleMapProvider;
        this.mathService = mathService;
    }

    @Override
    public void renderHealthBar(Unit unit) {
        Component healthBarComponent = MapsActivity.getComponent(HealthBarComponent.class);
        HealthBar healthBar = (HealthBar) healthBarComponent.getElement(unit.getId());
        if (healthBar == null) {
            if (unit.isVisible()) {
                healthBar = new HealthBar(unit.getId(), MapsActivity.getComponent(HealthBarComponent.class).getActivity().getApplicationContext());
                setHealthBarPosition(unit, healthBar);
                setHealthBarPercentage(unit, healthBar);
                healthBarComponent.addComponentElement(healthBar);
            }
        } else {
            if (!unit.isVisible()) {
                healthBarComponent.removeComponentElement(unit.getId());
            } else {
                setHealthBarPosition(unit, healthBar);
                setHealthBarPercentage(unit, healthBar);
            }
        }


        healthBarComponent.drawComponentElements();
    }

    private void setHealthBarPosition(Unit unit, HealthBar healthBar) {
        Point center = googleMapProvider.get().getGoogleMap().getProjection().toScreenLocation(unit.getCurrentPosition());
        Point edge = googleMapProvider.get().getGoogleMap().getProjection().toScreenLocation(SphericalUtil.computeOffset(unit.getCurrentPosition(), unit.getRadius(), 0d));
        double lineDistance = mathService.get().calculateLinearDistance(center, edge);

        int healthBarHeight = (int)(lineDistance * .75);
        int healthBarWidth = (int)(lineDistance * 2);
        int padding = (int)(lineDistance * .2);

        healthBar.setWidth(healthBarWidth);
        healthBar.setHeight(healthBarHeight);
        center.x -= lineDistance;
        center.y -= (lineDistance + healthBarHeight + padding);
        healthBar.setPoint(center);
    }

    private void setHealthBarPercentage(Unit unit, HealthBar healthBar) {
        healthBar.setHealthPercentage(((float)unit.getHealth()) / unit.getMaxHealth());
    }
}
