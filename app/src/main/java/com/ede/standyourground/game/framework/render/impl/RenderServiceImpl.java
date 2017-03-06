package com.ede.standyourground.game.framework.render.impl;

import android.graphics.Point;
import android.graphics.PointF;

import com.ede.standyourground.app.activity.MapsActivity;
import com.ede.standyourground.app.ui.Component;
import com.ede.standyourground.app.ui.HealthBar;
import com.ede.standyourground.app.ui.HealthBarComponent;
import com.ede.standyourground.framework.Logger;
import com.ede.standyourground.framework.api.MathService;
import com.ede.standyourground.framework.dagger.providers.GoogleMapProvider;
import com.ede.standyourground.game.framework.render.api.RenderService;
import com.ede.standyourground.game.model.Unit;
import com.google.android.gms.maps.Projection;
import com.google.maps.android.SphericalUtil;

import javax.inject.Inject;

import dagger.Lazy;

public class RenderServiceImpl implements RenderService {

    private static final Logger logger = new Logger(RenderServiceImpl.class);

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
        Projection projection = googleMapProvider.get().getGoogleMap().getProjection();
        Point center = projection.toScreenLocation(unit.getCurrentPosition());
        Point edge = projection.toScreenLocation(SphericalUtil.computeOffset(unit.getCurrentPosition(), unit.getRadius(), 0d));
        double lineDistance = mathService.get().calculateLinearDistance(center, edge);

        float healthBarHeight = (float)lineDistance * .75f;
        float healthBarWidth = (float)lineDistance * 2;
        float padding = (float)lineDistance * .2f;

        healthBar.setWidth(healthBarWidth);
        healthBar.setHeight(healthBarHeight);
        PointF pointf = new PointF();
        pointf.x = center.x - (float)lineDistance;
        pointf.y = center.y - (float)(lineDistance + healthBarHeight + padding);
        healthBar.setPoint(pointf);
    }

    private void setHealthBarPercentage(Unit unit, HealthBar healthBar) {
        healthBar.setHealthPercentage(((float)unit.getHealth()) / unit.getMaxHealth());
    }
}
