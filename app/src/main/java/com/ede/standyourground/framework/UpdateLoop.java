package com.ede.standyourground.framework;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import com.ede.standyourground.app.service.MathUtils;
import com.ede.standyourground.app.service.RouteUtil;
import com.ede.standyourground.game.model.MovableUnit;
import com.ede.standyourground.game.model.Unit;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eddie on 2/4/2017.
 */

public class UpdateLoop implements Runnable {

    private final Logger logger = new Logger(UpdateLoop.class);
    private static final long LOOP_DELAY = 16;

    private static Handler handler;

    private boolean stateChange = false;

    public void startLoop() {
        logger.i("Starting update thread");
        new Thread(this).start();
    }


    @Override
    public void run() {
        if (handler == null) {
            Looper.prepare();
            handler = new Handler();
            handler.postDelayed(this, LOOP_DELAY);
            logger.i("Update thread started");
            Looper.loop();
        }
        List<Unit> updatedUnits = new ArrayList<>();
        stateChange = false;
        for (Unit unit : WorldManager.getInstance().getUnits()) {
            if (unit instanceof  MovableUnit) {
                MovableUnit movableUnit = (MovableUnit) unit;
                long elapsed = SystemClock.uptimeMillis() - unit.getCreatedTime();
                int valuesTraveled = (int)Math.round((RouteUtil.milesToValue(movableUnit.getMph()) / 60d / 60 / 1000) * elapsed);

                int sumOfPreviousTargets = MathUtils.sumTo(movableUnit.getPath().getDistances(), movableUnit.getCurrentTarget());
                int distanceTraveledToTarget = valuesTraveled - sumOfPreviousTargets;
                double proportionToNextPoint = distanceTraveledToTarget / (double) movableUnit.getPath().getDistances().get(movableUnit.getCurrentTarget());

                LatLng currentPosition = movableUnit.getCurrentTarget() == 0 ? unit.getStartingPosition() : movableUnit.getPath().getPoints().get(movableUnit.getCurrentTarget() - 1);
                LatLng currentTarget = movableUnit.getPath().getPoints().get(movableUnit.getCurrentTarget());

                LatLng intermediatePosition = SphericalUtil.interpolate(currentPosition, currentTarget, proportionToNextPoint);

                movableUnit.setPosition(intermediatePosition);

                if (proportionToNextPoint >= 1) {
                    movableUnit.incrementTarget();
                    if (movableUnit.getCurrentTarget() >= movableUnit.getPath().getDistances().size()) {
                        movableUnit.setReachedEnemy(true);
                    }
                }

                stateChange = true;
                updatedUnits.add(movableUnit);
            }
        }
        if (stateChange) {
            UpdateLoopTask updateLoopTask = new UpdateLoopTask();
            updateLoopTask.setUpdatedUnits(updatedUnits);
            updateLoopTask.send();
        }

        handler.postDelayed(this, LOOP_DELAY);
    }
}
