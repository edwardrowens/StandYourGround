package com.ede.standyourground.game.framework.update.service.impl;

import android.os.SystemClock;

import com.ede.standyourground.framework.api.MathService;
import com.ede.standyourground.framework.api.RouteService;
import com.ede.standyourground.game.framework.management.impl.WorldManager;
import com.ede.standyourground.game.framework.update.service.api.UpdateService;
import com.ede.standyourground.game.model.MovableUnit;
import com.ede.standyourground.game.model.Path;
import com.ede.standyourground.game.model.Unit;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import javax.inject.Inject;

import dagger.Lazy;


public class UpdateServiceImpl implements UpdateService {

    private final Lazy<RouteService> routeService;
    private final Lazy<MathService> mathService;
    private final Lazy<WorldManager> worldManager;

    @Inject
    UpdateServiceImpl(Lazy<RouteService> routeService,
                      Lazy<MathService> mathService,
                      Lazy<WorldManager> worldManager) {
        this.routeService = routeService;
        this.mathService = mathService;
        this.worldManager = worldManager;
    }

    @Override
    public void determineVisibility(Unit unit) {
        if (!unit.isEnemy()) {
            for (Unit target : worldManager.get().getUnits().values()) {
                if (target.isEnemy()) {
                    boolean isVisible = mathService.get().withinDistance(unit.getCurrentPosition(), target.getCurrentPosition(), unit.getVisionRadius());
                    worldManager.get().getUnit(target.getId()).setIsVisible(isVisible);
                }
            }
        }
    }

    @Override
    public void determinePosition(Unit unit) {
        if (unit instanceof MovableUnit) {
            MovableUnit movableUnit = (MovableUnit) unit;
            long elapsed = SystemClock.uptimeMillis() - unit.getCreatedTime();
            int valuesTraveled = (int) Math.round((routeService.get().milesToValue(movableUnit.getMph()) / 60d / 60 / 1000) * elapsed);

            Path unitPath = new Path(unit.getWaypoints(), routeService.get().getDistanceOfSteps(unit.getWaypoints(), unit.getCurrentPosition()));
            int sumOfPreviousTargets = mathService.get().sumTo(unitPath.getDistances(), movableUnit.getCurrentTarget());
            int distanceTraveledToTarget = valuesTraveled - sumOfPreviousTargets;
            double proportionToNextPoint = distanceTraveledToTarget / (double) unitPath.getDistances().get(movableUnit.getCurrentTarget());

            LatLng currentPosition = movableUnit.getCurrentTarget() == 0 ? unit.getStartingPosition() : unitPath.getPoints().get(movableUnit.getCurrentTarget() - 1);
            LatLng currentTarget = unitPath.getPoints().get(movableUnit.getCurrentTarget());

            LatLng intermediatePosition = SphericalUtil.interpolate(currentPosition, currentTarget, proportionToNextPoint);

            movableUnit.setCurrentPosition(intermediatePosition);

            if (proportionToNextPoint >= 1) {
                movableUnit.incrementTarget();
            }

        }
    }
}
