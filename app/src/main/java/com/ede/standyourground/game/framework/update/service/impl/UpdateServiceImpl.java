package com.ede.standyourground.game.framework.update.service.impl;

import android.os.SystemClock;

import com.ede.standyourground.framework.Logger;
import com.ede.standyourground.framework.api.LatLngService;
import com.ede.standyourground.framework.api.RouteService;
import com.ede.standyourground.game.framework.management.impl.WorldManager;
import com.ede.standyourground.game.framework.update.service.api.UpdateService;
import com.ede.standyourground.game.model.MovableUnit;
import com.ede.standyourground.game.model.Unit;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.Collection;

import javax.inject.Inject;

import dagger.Lazy;


public class UpdateServiceImpl implements UpdateService {

    private static final Logger logger = new Logger(UpdateServiceImpl.class);

    private final Lazy<RouteService> routeService;
    private final Lazy<LatLngService> mathService;
    private final Lazy<WorldManager> worldManager;

    @Inject
    UpdateServiceImpl(Lazy<RouteService> routeService,
                      Lazy<LatLngService> mathService,
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
            long time = SystemClock.uptimeMillis();
            long elapsed = time - movableUnit.getLastUpdated();
            movableUnit.setLastUpdated(time);
            double distanceTraveled = (routeService.get().milesToValue(movableUnit.getMph()) / 60d / 60 / 1000) * elapsed;
            movableUnit.setDistanceTraveled(movableUnit.getDistanceTraveled() + distanceTraveled);

            int sumOfPreviousTargets = mathService.get().sumTo(movableUnit.getPath().getDistances(), movableUnit.getCurrentTarget());
            double distanceToNextTargetWaypoint = movableUnit.getDistanceTraveled() - sumOfPreviousTargets;
            double proportionToNextPoint = distanceToNextTargetWaypoint / movableUnit.getPath().getDistances().get(movableUnit.getCurrentTarget());

            LatLng currentPosition = movableUnit.getCurrentTarget() == 0 ? movableUnit.getStartingPosition() : movableUnit.getPath().getPoints().get(movableUnit.getCurrentTarget() - 1);
            LatLng currentTarget = movableUnit.getPath().getPoints().get(movableUnit.getCurrentTarget());

            LatLng intermediatePosition = SphericalUtil.interpolate(currentPosition, currentTarget, proportionToNextPoint);

            movableUnit.setCurrentPosition(intermediatePosition);

            if (proportionToNextPoint >= 1) {
                movableUnit.incrementTarget();
            }

        }
    }

    @Override
    public void processCombat(Collection<Unit> units) {
        for (Unit unit : units) {
            for (Unit target : units) {
            }
        }
    }
}
