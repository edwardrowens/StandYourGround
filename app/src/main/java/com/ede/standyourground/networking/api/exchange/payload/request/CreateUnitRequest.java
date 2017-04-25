package com.ede.standyourground.networking.api.exchange.payload.request;

import com.ede.standyourground.game.api.model.UnitType;
import com.ede.standyourground.networking.api.exchange.payload.Exchange;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class CreateUnitRequest extends Exchange {

    private UnitType unit;
    private List<LatLng> waypoints;
    private LatLng position;
    private final String type = CreateUnitRequest.class.getSimpleName();

    @Override
    public String getType() {
        return type;
    }

    public UnitType getUnit() {
        return unit;
    }

    public void setUnit(UnitType unit) {
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
