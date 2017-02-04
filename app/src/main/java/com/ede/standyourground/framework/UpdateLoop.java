package com.ede.standyourground.framework;

import android.os.SystemClock;
import android.view.animation.LinearInterpolator;

import com.ede.standyourground.game.model.MovableUnit;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Eddie on 2/4/2017.
 */

public class UpdateLoop implements Runnable {

    private final Map<UUID, MovableUnit> units = new ConcurrentHashMap();
    private final LinearInterpolator linearInterpolator = new LinearInterpolator();

    public void startLoop() {
        new Thread(this).start();
    }

    public void addUnit(MovableUnit unit) {
        units.put(unit.getId(), unit);
    }

    public MovableUnit getUnit(UUID id) {
        return units.get(id);
    }


    @Override
    public void run() {
        while (true) {

            for (MovableUnit unit : units.values()) {
                long elapsed = SystemClock.uptimeMillis() - unit.getCreatedTime();
                double t = linearInterpolator.getInterpolation((float) elapsed/unit.getSpeed());
            }
        }
    }
}
