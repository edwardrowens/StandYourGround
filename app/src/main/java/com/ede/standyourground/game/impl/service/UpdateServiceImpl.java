package com.ede.standyourground.game.impl.service;

import android.os.SystemClock;
import android.util.SparseIntArray;

import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.framework.api.service.LatLngService;
import com.ede.standyourground.framework.api.service.RouteService;
import com.ede.standyourground.game.api.model.Attacker;
import com.ede.standyourground.game.api.model.Healable;
import com.ede.standyourground.game.api.model.Healer;
import com.ede.standyourground.game.api.model.Hostility;
import com.ede.standyourground.game.api.model.MovableUnit;
import com.ede.standyourground.game.api.model.Unit;
import com.ede.standyourground.game.api.service.PlayerService;
import com.ede.standyourground.game.api.service.UnitService;
import com.ede.standyourground.game.api.service.UpdateService;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;


public class UpdateServiceImpl implements UpdateService {

    private static final Logger logger = new Logger(UpdateServiceImpl.class);

    private final Lazy<RouteService> routeService;
    private final Lazy<LatLngService> latLngService;
    private final Lazy<UnitService> unitService;
    private final Lazy<PlayerService> playerService;

    @Inject
    UpdateServiceImpl(Lazy<RouteService> routeService,
                      Lazy<LatLngService> latLngService,
                      Lazy<UnitService> unitService,
                      Lazy<PlayerService> playerService) {
        this.routeService = routeService;
        this.latLngService = latLngService;
        this.unitService = unitService;
        this.playerService = playerService;
    }

    @Override
    public void determineVisibility(Unit unit) {
        if (unit.getHostility() != Hostility.FRIENDLY) {
            List<Unit> units = unitService.get().getUnits();
            boolean visible = false;
            for (int i = 0; i < units.size() && !visible; ++i) {
                Unit target = units.get(i);
                if (target.getHostility() == Hostility.FRIENDLY) {
                    LatLng unitPosition = unit instanceof MovableUnit ? ((MovableUnit) unit).getCurrentPosition() : unit.getStartingPosition();
                    LatLng targetPosition = target instanceof MovableUnit ? ((MovableUnit) target).getCurrentPosition() : target.getStartingPosition();

                    visible = latLngService.get().withinDistance(unitPosition, targetPosition, target.getVisionRadius() + unit.getRadius());
                }
            }
            unitService.get().getUnit(unit.getId()).setIsVisible(visible);
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
        // key = who died, value = who killed them
        SparseIntArray deadTargets = new SparseIntArray();
        for (int i = 0; i < units.size(); ++i) {
            Unit unit = units.get(i);
            if (deadTargets.get(i, -1) == i || !unit.isAlive()) {
                continue;
            }
            Unit interactionTarget = null;
            int j = 0;
            for (; j < units.size() && interactionTarget == null; ++j) {
                Unit target = units.get(j);
                if (deadTargets.get(j, -1) == j || !target.isAlive()) {
                    continue;
                }
                LatLng unitPosition = unit instanceof MovableUnit ? ((MovableUnit) unit).getCurrentPosition() : unit.getStartingPosition();
                LatLng targetPosition = target instanceof MovableUnit ? ((MovableUnit) target).getCurrentPosition() : target.getStartingPosition();
                double distance = latLngService.get().calculateDistance(unitPosition, targetPosition);
                if (unit instanceof Attacker) {
                    if (((Attacker) unit).canAttack(target, distance)) {
                        interactionTarget = target;
                        ((Attacker) unit).combat(target);
                    }
                } else if (unit instanceof Healer) {
                    if (((Healer) unit).canHeal(target, distance)) {
                        logger.i("%s is healing %s.", unit.getId(), target.getHostility().toString());
                        ((Healer) unit).heal((Healable) target);
                        interactionTarget = target;
                    }
                }
            }
            if (interactionTarget != null) {
                if (!interactionTarget.isAlive()) {
                    deadTargets.put(j - 1, i);
                }
            } else {
                if (unit instanceof MovableUnit && ((MovableUnit) unit).getMph() == 0d) {
                    ((MovableUnit) unit).move();
                }
            }
        }

        for (int i = 0; i < deadTargets.size(); ++i) {
            int deadUnit = deadTargets.keyAt(i);
            int killingUnit = deadTargets.valueAt(i);
            units.get(deadUnit).onDeath(units.get(killingUnit));
        }
    }

    @Override
    public void calculateResourceAccrual() {
        long lastResourceAccrual = playerService.get().getLastResourceAccrual();
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastResourceAccrual >= 10000) {
            playerService.get().accrueIncome();
        }
    }
}
