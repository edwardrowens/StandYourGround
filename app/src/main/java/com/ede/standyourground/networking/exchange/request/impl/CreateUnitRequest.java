package com.ede.standyourground.networking.exchange.request.impl;

import com.ede.standyourground.game.model.Units;
import com.ede.standyourground.networking.exchange.api.Exchange;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class CreateUnitRequest extends Exchange {

    private Units unit;
    private List<LatLng> waypoints;
    private LatLng position;
    private final String type = CreateUnitRequest.class.getSimpleName();

    @Override
    public String getType() {
        return type;
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
