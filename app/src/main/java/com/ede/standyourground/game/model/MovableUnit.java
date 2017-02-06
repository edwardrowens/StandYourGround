package com.ede.standyourground.game.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by Eddie on 2/3/2017.
 */

public class MovableUnit extends Unit implements Movable {

    private int speed;
    private Path path;
    private AtomicInteger currentTarget = new AtomicInteger(0);

    public MovableUnit(int speed, Path path, LatLng position) {
        super(position);
        this.speed = speed;
        this.path = path;
    }

    @Override
    public void move(long elapsedTime) {
    }

    public int getSpeed() {
        return speed;
    }

    public void incrementTarget() {
        if (currentTarget.get() < path.getPoints().size() - 1) {
            currentTarget.incrementAndGet();
        }
    }

    public LatLng getTarget() {
        return path.getPoints().get(path.getPoints().size() - 1);
    }

    public boolean reachedEnemy() {
        return currentTarget.get() >= path.getPoints().size();
    }

    public void setPosition(LatLng position) {
        this.position.set(position);
    }
}
