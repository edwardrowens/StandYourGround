package com.ede.standyourground.game.model;

import com.ede.standyourground.framework.api.RouteService;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;


public abstract class MovableUnit extends Unit {

    private double mph;
    private Path path;
    private long arrivalTime;
    private int totalDistance;
    private int currentTarget;
    private AtomicReference<LatLng> position;

    @Inject RouteService routeService;

    public MovableUnit(double mph, List<LatLng> waypoints, LatLng position, boolean isEnemy) {
        super(position, waypoints, isEnemy);
        this.path = new Path(waypoints, routeService.getDistanceOfSteps(waypoints, position));
        this.mph = mph;
        this.totalDistance = routeService.calculateTotalDistance(path);
        arrivalTime = routeService.timeToDestination(routeService.valueToMiles(totalDistance), mph);
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
