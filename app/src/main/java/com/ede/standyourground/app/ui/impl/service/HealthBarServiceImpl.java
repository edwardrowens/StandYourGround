package com.ede.standyourground.app.ui.impl.service;

import android.graphics.Point;
import android.graphics.PointF;

import com.ede.standyourground.app.ui.api.service.HealthBarService;
import com.ede.standyourground.app.ui.impl.component.HealthBar;
import com.ede.standyourground.framework.api.dagger.providers.GoogleMapProvider;
import com.ede.standyourground.framework.api.service.ProjectionService;
import com.ede.standyourground.game.api.model.MovableUnit;
import com.ede.standyourground.game.api.model.Unit;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;

import dagger.Lazy;

/**
 *
 */

public class HealthBarServiceImpl implements HealthBarService {

    private final Lazy<GoogleMapProvider> googleMapProvider;
    private final Lazy<ProjectionService> projectionService;

    @Inject
    HealthBarServiceImpl(Lazy<GoogleMapProvider> googleMapProvider,
                         Lazy<ProjectionService> projectionService) {
        this.googleMapProvider = googleMapProvider;
        this.projectionService = projectionService;
    }

    @Override
    public void setHealthBarPosition(Unit unit, HealthBar healthBar) {
        Projection projection = googleMapProvider.get().getGoogleMap().getProjection();
        LatLng unitPosition = unit instanceof MovableUnit ? ((MovableUnit) unit).getCurrentPosition() : unit.getStartingPosition();
        Point center = projection.toScreenLocation(unitPosition);
        double lineDistance = projectionService.get().calculateUnitPixelRadius(projection, unit);

        float healthBarHeight = (float) lineDistance * .75f;
        float healthBarWidth = (float) lineDistance * 2;
        float padding = (float) lineDistance * .2f;

        healthBar.setWidth(healthBarWidth);
        healthBar.setHeight(healthBarHeight);
        PointF pointf = new PointF();
        pointf.x = center.x - (float) lineDistance;
        pointf.y = center.y - (float) (lineDistance + healthBarHeight + padding);
        healthBar.setPoint(pointf);
    }
}
