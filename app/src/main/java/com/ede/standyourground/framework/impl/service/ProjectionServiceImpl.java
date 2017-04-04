package com.ede.standyourground.framework.impl.service;

import android.graphics.Point;

import com.ede.standyourground.framework.api.service.MathService;
import com.ede.standyourground.framework.api.service.ProjectionService;
import com.ede.standyourground.game.api.model.MovableUnit;
import com.ede.standyourground.game.api.model.Unit;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import javax.inject.Inject;

import dagger.Lazy;

/**
 *
 */

public class ProjectionServiceImpl implements ProjectionService {

    private final Lazy<MathService> mathService;

    @Inject
    ProjectionServiceImpl(Lazy<MathService> mathService) {
        this.mathService = mathService;
    }

    @Override
    public double calculateUnitPixelRadius(Projection projection, Circle circle) {
        Point center = projection.toScreenLocation(circle.getCenter());
        Point edge = projection.toScreenLocation(SphericalUtil.computeOffset(circle.getCenter(), circle.getRadius(), 0d));
        return mathService.get().calculateLinearDistance(center, edge);
    }

    @Override
    public double calculateUnitPixelRadius(Projection projection, Unit unit) {
        LatLng unitPosition = unit instanceof MovableUnit ? ((MovableUnit) unit).getCurrentPosition() : unit.getStartingPosition();
        Point center = projection.toScreenLocation(unitPosition);
        Point edge = projection.toScreenLocation(SphericalUtil.computeOffset(unitPosition, unit.getRadius(), 0d));
        return mathService.get().calculateLinearDistance(center, edge);
    }
}
