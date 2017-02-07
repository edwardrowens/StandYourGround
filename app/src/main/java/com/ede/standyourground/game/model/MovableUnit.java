package com.ede.standyourground.game.model;

import com.ede.standyourground.app.service.RouteUtil;
import com.ede.standyourground.framework.Logger;
import com.google.android.gms.maps.model.LatLng;

import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Created by Eddie on 2/3/2017.
 */

public class MovableUnit extends Unit {

    private static Logger logger = new Logger(MovableUnit.class);

    private double mph;
    private Path path;
    private long arrivalTime;
    private AtomicBoolean reachedEnemy;

    public MovableUnit(double mph, Path path, LatLng position) {
        super(position);
        this.path = path;
        this.mph = mph;
        int totalDistance = RouteUtil.calculateTotalDistance(path);
        logger.d("totalDistance is %d", totalDistance);
        arrivalTime = RouteUtil.timeToDestination(RouteUtil.valueToMiles(totalDistance), mph);
        logger.d("arrivalTime is %d", arrivalTime);
        reachedEnemy = new AtomicBoolean(false);
    }

    public Path getPath() {
        return path;
    }

    public void setPosition(LatLng position) {
        this.position.set(position);
    }

    public double getMph() {
        return mph;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    public boolean getReachedEnemy() {
        return reachedEnemy.get();
    }

    public void setReachedEnemy(boolean reachedEnemy) {
        this.reachedEnemy.set(reachedEnemy);
    }
}
