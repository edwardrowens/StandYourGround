package com.ede.standyourground.game.framework.update.impl;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;

import com.ede.standyourground.app.service.MathUtils;
import com.ede.standyourground.app.service.RouteUtil;
import com.ede.standyourground.framework.Logger;
import com.ede.standyourground.game.framework.management.impl.WorldManager;
import com.ede.standyourground.game.framework.render.api.Renderer;
import com.ede.standyourground.game.model.MovableUnit;
import com.ede.standyourground.game.model.Unit;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;


public class UpdateLoop implements Runnable {

    private static final Logger logger = new Logger(UpdateLoop.class);
    private static final long LOOP_DELAY = 16;

    private final HandlerThread loopThread = new HandlerThread("UpdateLoop");
    private Handler handler;
    private Renderer renderer;

    public void startLoop(Renderer renderer) {
        this.renderer = renderer;

        logger.i("Starting update thread");
        loopThread.start();
        handler = new Handler(loopThread.getLooper());
        handler.post(this);
    }

    @Override
    public void run() {
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
                }

            }
            renderer.render(unit);
        }

        handler.postDelayed(this, LOOP_DELAY);
    }
}
