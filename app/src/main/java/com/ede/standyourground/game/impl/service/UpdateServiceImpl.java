package com.ede.standyourground.game.impl.service;

import android.os.SystemClock;

import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.framework.api.service.LatLngService;
import com.ede.standyourground.framework.api.service.RouteService;
import com.ede.standyourground.game.api.model.Attacker;
import com.ede.standyourground.game.api.model.Cell;
import com.ede.standyourground.game.api.model.Healable;
import com.ede.standyourground.game.api.model.Healer;
import com.ede.standyourground.game.api.model.Hostility;
import com.ede.standyourground.game.api.model.MovableUnit;
import com.ede.standyourground.game.api.model.Unit;
import com.ede.standyourground.game.api.service.PlayerService;
import com.ede.standyourground.game.api.service.UnitService;
import com.ede.standyourground.game.api.service.UpdateService;
import com.ede.standyourground.game.api.service.WorldGridService;
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
    private final Lazy<WorldGridService> worldGridService;

    @Inject
    UpdateServiceImpl(Lazy<RouteService> routeService,
                      Lazy<LatLngService> latLngService,
                      Lazy<UnitService> unitService,
                      Lazy<PlayerService> playerService,
                      Lazy<WorldGridService> worldGridService) {
        this.routeService = routeService;
        this.latLngService = latLngService;
        this.unitService = unitService;
        this.playerService = playerService;
        this.worldGridService = worldGridService;
    }

    @Override
    public void determineVisibility(Unit unit) {
        if (unit.getHostility() == Hostility.FRIENDLY) {
            return;
        }

        List<Unit> unitsInRange = worldGridService.get().retrieveUnitsInCellRange(unit.getCell(), unit.getVisionRadius());

        boolean visible = false;
        for (int i = 0; i < unitsInRange.size() && !visible; ++i) {
            Unit target = unitsInRange.get(i);
            if (target.getHostility() == Hostility.FRIENDLY) {
                LatLng unitPosition = unit instanceof MovableUnit ? ((MovableUnit) unit).getCurrentPosition() : unit.getStartingPosition();
                LatLng targetPosition = target instanceof MovableUnit ? ((MovableUnit) target).getCurrentPosition() : target.getStartingPosition();

                visible = latLngService.get().withinDistance(unitPosition, targetPosition, target.getVisionRadius() + unit.getRadius());
            }
        }
        unitService.get().getUnit(unit.getId()).setIsVisible(visible);
    }

    @Override
    public void determinePosition(Unit unit) {
        if (!(unit instanceof MovableUnit)) {
            return;
        }
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

        Cell cell = worldGridService.get().calculateCellPosition(intermediatePosition);
        worldGridService.get().moveUnitCell(unit.getCell(), cell, unit);

        movableUnit.setCurrentPosition(intermediatePosition);
        unit.setCell(cell);

        if (proportionToNextPoint >= 1) {
            movableUnit.incrementTarget();
        }

    }

    @Override
    public Unit processCombat(Unit unit) {
        if (!(unit instanceof Attacker) && !(unit instanceof Healer)) {
            return null;
        }

        List<Unit> unitsInRange;
        if (unit instanceof Attacker) {
            unitsInRange = worldGridService.get().retrieveUnitsInCellRange(unit.getCell(), ((Attacker) unit).getAttackRange());
        } else {
            unitsInRange = worldGridService.get().retrieveUnitsInCellRange(unit.getCell(), ((Healer) unit).getHealRange());
        }

        for (Unit target : unitsInRange) {
            LatLng unitPosition = unit instanceof MovableUnit ? ((MovableUnit) unit).getCurrentPosition() : unit.getStartingPosition();
            LatLng targetPosition = target instanceof MovableUnit ? ((MovableUnit) target).getCurrentPosition() : target.getStartingPosition();
            double distance = latLngService.get().calculateDistance(unitPosition, targetPosition);
            if (unit instanceof Attacker) {
                if (((Attacker) unit).canAttack(target, distance)) {
                    ((Attacker) unit).combat(target);
                    return target;
                }
            } else if (((Healer) unit).canHeal(target, distance)) {
                ((Healer) unit).heal((Healable) target);
                return target;
            }
        }

        if (unit instanceof MovableUnit && ((MovableUnit) unit).getMph() == 0d) {
            ((MovableUnit) unit).move();
        }

        return null;
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
