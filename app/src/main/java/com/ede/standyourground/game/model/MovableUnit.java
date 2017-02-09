package com.ede.standyourground.game.model;

import com.ede.standyourground.app.service.RouteUtil;
import com.ede.standyourground.framework.Logger;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;


/**
 * Created by Eddie on 2/3/2017.
 */

public class MovableUnit extends Unit {

    private static Logger logger = new Logger(MovableUnit.class);

    private double mph;
    private Path path;
    private long arrivalTime;
    private AtomicBoolean reachedEnemy;
    private int totalDistance;
    private int currentTarget;
    private AtomicReference<LatLng> position;

    public MovableUnit(double mph, Polyline polyline, LatLng position, Circle circle) {
        super(position, circle, polyline);
        this.path = new Path(polyline.getPoints(), RouteUtil.getDistanceOfSteps(polyline.getPoints(), position));
        this.mph = mph;
        this.totalDistance = RouteUtil.calculateTotalDistance(path);
        arrivalTime = RouteUtil.timeToDestination(RouteUtil.valueToMiles(totalDistance), mph);
        reachedEnemy = new AtomicBoolean(false);
        currentTarget = 0;
        this.position = new AtomicReference<>(this.getStartingPosition());
    }

    public Path getPath() {
        return path;
    }

    public void setPosition(LatLng position) {
        this.position.set(position);
    }

    public LatLng getPosition() {
        return position.get();
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

    public int getTotalDistance() {
        return totalDistance;
    }

    public void incrementTarget() {
        if (currentTarget < path.getDistances().size() - 1)
            ++currentTarget;
    }

    public int getCurrentTarget() {
        return currentTarget;
    }
}
