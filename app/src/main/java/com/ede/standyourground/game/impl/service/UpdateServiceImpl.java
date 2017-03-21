package com.ede.standyourground.game.impl.service;

import android.os.SystemClock;

import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.framework.api.service.LatLngService;
import com.ede.standyourground.framework.api.service.RouteService;
import com.ede.standyourground.game.api.model.Attacker;
import com.ede.standyourground.game.api.model.MovableUnit;
import com.ede.standyourground.game.api.model.Unit;
import com.ede.standyourground.game.api.service.UpdateService;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;


public class UpdateServiceImpl implements UpdateService {

    private static final Logger logger = new Logger(UpdateServiceImpl.class);

    private final Lazy<RouteService> routeService;
    private final Lazy<LatLngService> latLngService;
    private final Lazy<UnitServiceImpl> worldManager;

    @Inject
    UpdateServiceImpl(Lazy<RouteService> routeService,
                      Lazy<LatLngService> latLngService,
                      Lazy<UnitServiceImpl> worldManager) {
        this.routeService = routeService;
        this.latLngService = latLngService;
        this.worldManager = worldManager;
    }

    @Override
    public void determineVisibility(Unit unit) {
        if (unit.isEnemy()) {
            List<Unit> units = worldManager.get().getUnits();
            boolean visible = false;
            for (int i = 0; i < units.size() && !visible; ++i) {
                Unit target = units.get(i);
                if (!target.isEnemy()) {
                    LatLng unitPosition = unit instanceof MovableUnit ? ((MovableUnit) unit).getCurrentPosition() : unit.getStartingPosition();
                    LatLng targetPosition = target instanceof MovableUnit ? ((MovableUnit) target).getCurrentPosition() : target.getStartingPosition();

                    visible = latLngService.get().withinDistance(unitPosition, targetPosition, target.getVisionRadius() + unit.getRadius());
                }
            }
            worldManager.get().getUnit(unit.getId()).setIsVisible(visible);
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

            int sumOfPreviousTargets = latLngService.get().sumTo(movableUnit.getPath().getDistances(), movableUnit.getCurrentTarget());
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
    public void processCombat(List<Unit> units) {
        List<Integer> deadTargets = new ArrayList<>();
        for (int i = 0; i < units.size(); ++i) {
            Unit unit = units.get(i);
            if (deadTargets.contains(i) || !unit.isAlive()) {
                continue;
            }
            Unit attackTarget = null;
            int j = 0;
            for (; j < units.size() && attackTarget == null; ++j) {
                Unit target = units.get(j);
                if (deadTargets.contains(j) || !target.isAlive()) {
                    continue;
                }
                LatLng unitPosition = unit instanceof MovableUnit ? ((MovableUnit) unit).getCurrentPosition() : unit.getStartingPosition();
                LatLng targetPosition = target instanceof MovableUnit ? ((MovableUnit) target).getCurrentPosition() : target.getStartingPosition();
                double distance = latLngService.get().calculateDistance(unitPosition, targetPosition);
                if (unit instanceof Attacker) {
                    if (((Attacker) unit).canAttack(target, distance)) {
                        attackTarget = target;
                        ((Attacker) unit).combat(target);
                    }
                }
            }
            if (attackTarget == null && unit instanceof MovableUnit && ((MovableUnit) unit).getMph() == 0d) {
                ((MovableUnit) unit).move();
            } else if (attackTarget != null && !attackTarget.isAlive()) {
                deadTargets.add(j - 1);
            }
        }

        for (int i : deadTargets) {
            units.get(i).onDeath();
        }
    }
}