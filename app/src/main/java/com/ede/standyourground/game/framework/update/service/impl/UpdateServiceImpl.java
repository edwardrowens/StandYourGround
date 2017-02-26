package com.ede.standyourground.game.framework.update.service.impl;

import android.os.SystemClock;

import com.ede.standyourground.framework.api.MathService;
import com.ede.standyourground.framework.api.RouteService;
import com.ede.standyourground.game.framework.update.service.api.UpdateService;
import com.ede.standyourground.game.model.MovableUnit;
import com.ede.standyourground.game.model.Unit;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;


public class UpdateServiceImpl implements UpdateService {

    private final Lazy<RouteService> routeService;
    private final Lazy<MathService> mathService;

    @Inject
    UpdateServiceImpl(Lazy<RouteService> routeService,
                      Lazy<MathService> mathService) {
        this.routeService = routeService;
        this.mathService = mathService;
    }

    @Override
    public void determineVisibility(List<Unit> units) {

    }

    @Override
    public void determinePosition(Unit unit) {
        if (unit instanceof MovableUnit) {
            MovableUnit movableUnit = (MovableUnit) unit;
            long elapsed = SystemClock.uptimeMillis() - unit.getCreatedTime();
            int valuesTraveled = (int)Math.round((routeService.get().milesToValue(movableUnit.getMph()) / 60d / 60 / 1000) * elapsed);

            int sumOfPreviousTargets = mathService.get().sumTo(movableUnit.getPath().getDistances(), movableUnit.getCurrentTarget());
            int distanceTraveledToTarget = valuesTraveled - sumOfPreviousTargets;
            double proportionToNextPoint = distanceTraveledToTarget / (double) movableUnit.getPath().getDistances().get(movableUnit.getCurrentTarget());

            LatLng currentPosition = movableUnit.getCurrentTarget() == 0 ? unit.getStartingPosition() : movableUnit.getPath().getPoints().get(movableUnit.getCurrentTarget() - 1);
            LatLng currentTarget = movableUnit.getPath().getPoints().get(movableUnit.getCurrentTarget());

            LatLng intermediatePosition = SphericalUtil.interpolate(currentPosition, currentTarget, proportionToNextPoint);

            movableUnit.setPosition(intermediatePosition);

            if (proportionToNextPoint >= 1) {
                movableUnit.incrementTarget();
            }

        }
    }
}
