package com.ede.standyourground.networking.exchange.request.impl;

import com.ede.standyourground.game.model.Units;
import com.ede.standyourground.networking.exchange.request.api.Request;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.UUID;

/**
 * Created by Eddie on 2/15/2017.
 */

public class CreateUnitRequest implements Request {

    private long timestamp;
    private Units unit;
    private List<LatLng> waypoints;
    private LatLng position;
    private final UUID id = UUID.randomUUID();

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public Units getUnit() {
        return unit;
    }

    public void setUnit(Units unit) {
        this.unit = unit;
    }

    public List<LatLng> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<LatLng> waypoints) {
        this.waypoints = waypoints;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }
}
