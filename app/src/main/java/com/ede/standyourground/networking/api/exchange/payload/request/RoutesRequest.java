package com.ede.standyourground.networking.api.exchange.payload.request;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Eddie on 1/30/2017.
 */

public class RoutesRequest {

    @SerializedName("startLat")
    @Expose
    private Double startLat;

    @SerializedName("startLng")
    @Expose
    private Double startLng;

    @SerializedName("endLat")
    @Expose
    private Double endLat;

    @SerializedName("endLng")
    @Expose
    private Double endLng;

    @SerializedName("waypoints")
    @Expose
    private List<LatLng> waypoints;

    public Double getStartLat() {
        return startLat;
    }

    public void setStartLat(Double startLat) {
        this.startLat = startLat;
    }

    public Double getStartLng() {
        return startLng;
    }

    public void setStartLng(Double startLng) {
        this.startLng = startLng;
    }

    public Double getEndLat() {
        return endLat;
    }

    public void setEndLat(Double endLat) {
        this.endLat = endLat;
    }

    public Double getEndLng() {
        return endLng;
    }

    public void setEndLng(Double endLng) {
        this.endLng = endLng;
    }

    public List<LatLng> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<LatLng> waypoints) {
        this.waypoints = waypoints;
    }
}
