package com.ede.standyourground.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Eddie on 1/30/2017.
 */

public class Step {


    @SerializedName("distance")
    @Expose
    private DirectionsPair distance;

    @SerializedName("duration")
    @Expose
    private DirectionsPair duration;

    @SerializedName("end_location")
    @Expose
    private Bound endLocation;

    @SerializedName("polyline")
    @Expose
    private Polyline polyline;

    @SerializedName("start_location")
    @Expose
    private Bound startLocation;

    @SerializedName("travel_mode")
    @Expose
    private String travelMode;

    @SerializedName("maneuver")
    @Expose
    private String maneuver;

    public DirectionsPair getDistance() {
        return distance;
    }

    public void setDistance(DirectionsPair distance) {
        this.distance = distance;
    }

    public DirectionsPair getDuration() {
        return duration;
    }

    public void setDuration(DirectionsPair duration) {
        this.duration = duration;
    }

    public Bound getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Bound endLocation) {
        this.endLocation = endLocation;
    }

    public Polyline getPolyline() {
        return polyline;
    }

    public void setPolyline(Polyline polyline) {
        this.polyline = polyline;
    }

    public Bound getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Bound startLocation) {
        this.startLocation = startLocation;
    }

    public String getTravelMode() {
        return travelMode;
    }

    public void setTravelMode(String travelMode) {
        this.travelMode = travelMode;
    }

    public String getManeuver() {
        return maneuver;
    }

    public void setManeuver(String maneuver) {
        this.maneuver = maneuver;
    }
}
