package com.ede.standyourground.game.framework.render.impl;

import android.graphics.Point;

import com.ede.standyourground.app.activity.MapsActivity;
import com.ede.standyourground.app.ui.Component;
import com.ede.standyourground.app.ui.HealthBar;
import com.ede.standyourground.app.ui.HealthBarComponent;
import com.ede.standyourground.framework.Logger;
import com.ede.standyourground.framework.dagger.providers.GoogleMapProvider;
import com.ede.standyourground.game.framework.render.api.RenderService;
import com.ede.standyourground.game.model.Unit;

import javax.inject.Inject;

import dagger.Lazy;

public class RenderServiceImpl implements RenderService {

    private static final Logger logger = new Logger(RenderServiceImpl.class);
    private static final int HEALTH_BAR_PADDING = 10;


    private final Lazy<GoogleMapProvider> googleMapProvider;

    @Inject
    RenderServiceImpl(Lazy<GoogleMapProvider> googleMapProvider) {
        this.googleMapProvider = googleMapProvider;
    }

    @Override
    public void renderHealthBar(Unit unit) {
        Component healthBarComponent = MapsActivity.getComponent(HealthBarComponent.class);
        Point point = createPoint(unit);
        HealthBar healthBar = (HealthBar) healthBarComponent.getElement(unit.getId());
        if (healthBar == null) {
            if (unit.isVisible()) {
                healthBar = new HealthBar(unit.getId(), MapsActivity.getComponent(HealthBarComponent.class).getActivity().getApplicationContext());
                healthBar.setPoint(point);
                healthBarComponent.addComponentElement(healthBar);
            }
        } else {
            if (!unit.isVisible()) {
                healthBarComponent.removeComponentElement(unit.getId());
            } else {
                healthBar.setPoint(point);
            }
        }


        healthBarComponent.drawComponentElements();
    }

    Point createPoint(Unit unit) {
        Point point = googleMapProvider.get().getGoogleMap().getProjection().toScreenLocation(unit.getCurrentPosition());
        point.x -= unit.getRadius();
        point.y -= (unit.getRadius() + HealthBar.HEIGHT + HEALTH_BAR_PADDING);
        return point;
    }
}
