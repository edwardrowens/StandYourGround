package com.ede.standyourground.game.service;

import com.ede.standyourground.app.service.RouteUtil;
import com.ede.standyourground.framework.Logger;
import com.ede.standyourground.game.model.MovableUnit;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Eddie on 2/6/2017.
 */

public class UnitUtil {
    private static Logger logger = new Logger(UnitUtil.class);

    public static LatLng findNextTarget(MovableUnit movableUnit, long timeElapsed) {
        int valuesTraveled = (int)Math.round((RouteUtil.milesToValue(movableUnit.getMph()) / 60d / 60 / 1000) * timeElapsed);
        int index = 0;
        int valueSum = 0;
        int size = movableUnit.getPath().getDistances().size();
        for (;index < size && valuesTraveled > valueSum; ++index) {
            valueSum += movableUnit.getPath().getDistances().get(index);
        }

        logger.d("next target is %d", index);
        logger.d("value sum is %d", valueSum);
        logger.d("valuesTraveled is %d", valuesTraveled);
        return movableUnit.getPath().getPoints().get(index);
    }
}
