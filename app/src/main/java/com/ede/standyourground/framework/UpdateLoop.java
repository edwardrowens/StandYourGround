package com.ede.standyourground.framework;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.animation.LinearInterpolator;

import com.ede.standyourground.game.model.MovableUnit;
import com.ede.standyourground.game.service.UnitUtil;
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
    private static final long LOOP_DELAY = 16;

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
            long elapsed = SystemClock.uptimeMillis() - unit.getCreatedTime();
//            double t = linearInterpolator.getInterpolation((float) elapsed / unit.getArrivalTime());
            LatLng intermediatePosition = SphericalUtil.interpolate(unit.getPosition(), UnitUtil.findNextTarget(unit, elapsed), (float) elapsed / unit.getArrivalTime());

            unit.setPosition(intermediatePosition);
            stateChange = true;
            updatedUnits.add(unit);
            if ((float) elapsed / unit.getArrivalTime() >= 1) {
                unit.setReachedEnemy(true);
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
