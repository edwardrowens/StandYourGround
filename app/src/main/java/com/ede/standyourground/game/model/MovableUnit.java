package com.ede.standyourground.game.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by Eddie on 2/3/2017.
 */

public class MovableUnit extends Unit implements Movable {

    private int speed;
    private List<LatLng> path;
    private AtomicInteger currentTarget = new AtomicInteger(0);

    public MovableUnit(int speed, List<LatLng> path, LatLng position) {
        super(position);
        this.speed = speed;
        this.path = Collections.synchronizedList(path);
    }

    @Override
    public void move(long elapsedTime) {
    }

    public int getSpeed() {
        return speed / path.size();
    }

    public void incrementTarget() {
        if (currentTarget.get() < path.size() - 1) {
            currentTarget.incrementAndGet();
        }
    }

    public LatLng getTarget() {
        return path.get(currentTarget.get());
    }

    public boolean reachedEnemy() {
        return currentTarget.get() >= path.size();
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }
}
