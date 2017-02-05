package com.ede.standyourground.framework;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.animation.LinearInterpolator;

import com.ede.standyourground.game.model.MovableUnit;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Eddie on 2/4/2017.
 */

public class UpdateLoop implements Runnable {

    private final Logger logger = new Logger(UpdateLoop.class);
    private static final long LOOP_DELAY = 10;

    private static final Map<UUID, MovableUnit> units = new ConcurrentHashMap();
    private final LinearInterpolator linearInterpolator = new LinearInterpolator();

    private static Handler handler;

    private boolean stateChange = false;

    public void startLoop() {
        logger.i("Starting update thread");
        new Thread(this).start();
    }

    public void addUnit(MovableUnit unit) {
        units.put(unit.getId(), unit);
        logger.i("Added unit. Size is now %d", units.size());
    }


    @Override
    public void run() {
        if (handler == null) {
            Looper.prepare();
            handler = new Handler();
            handler.postDelayed(this, LOOP_DELAY);
            Looper.loop();
        }
        List<MovableUnit> updatedUnits = new ArrayList<>();
        stateChange = false;
        for (MovableUnit unit : units.values()) {
            long elapsed = SystemClock.uptimeMillis() - unit.getArrivalTime();
            stateChange = true;
            logger.i("interpolation: %.7f",(float) elapsed / unit.getSpeed());
            double t = linearInterpolator.getInterpolation((float) elapsed / unit.getSpeed());
            LatLng intermediatePosition = SphericalUtil.interpolate(unit.getPosition(), unit.getTarget(), t);

            unit.setPosition(intermediatePosition);
            updatedUnits.add(unit);
            if (t >= 1 && !unit.reachedEnemy()) {
                unit.setArrivalTime(SystemClock.uptimeMillis());
                unit.incrementTarget();
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
