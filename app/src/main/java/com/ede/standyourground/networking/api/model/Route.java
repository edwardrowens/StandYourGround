package com.ede.standyourground.networking.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Eddie on 1/30/2017.
 */

public class Route {

    @SerializedName("bounds")
    @Expose
    private Bounds bounds;

    @SerializedName("copyrights")
    @Expose
    private String copyrights;

    @SerializedName("legs")
    @Expose
    private List<Leg> legs;

    @SerializedName("overview_polyline")
    @Expose
    private Polyline overviewPolyline;

    @SerializedName("summary")
    @Expose
    private String summary;

    @SerializedName("warnings")
    @Expose
    private List<String> warnings;

    @SerializedName("waypoint_order")
    @Expose
    private List<Integer> waypointOrder;

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    public List<Leg> getLegs() {
        return legs;
    }

    public void setLegs(List<Leg> legs) {
        this.legs = legs;
    }

    public String getCopyrights() {
        return copyrights;
    }

    public void setCopyrights(String copyrights) {
        this.copyrights = copyrights;
    }

    public Polyline getOverviewPolyline() {
        return overviewPolyline;
    }

    public void setOverviewPolyline(Polyline overviewPolyline) {
        this.overviewPolyline = overviewPolyline;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    public List<Integer> getWaypointOrder() {
        return waypointOrder;
    }

    public void setWaypointOrder(List<Integer> waypointOrder) {
        this.waypointOrder = waypointOrder;
    }
}
