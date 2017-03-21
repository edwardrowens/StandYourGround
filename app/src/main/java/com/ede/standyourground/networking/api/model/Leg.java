package com.ede.standyourground.networking.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Eddie on 1/30/2017.
 */

public class Leg {

    @SerializedName("distance")
    @Expose
    private DirectionsPair distance;

    @SerializedName("duration")
    @Expose
    private DirectionsPair duration;

    @SerializedName("end_address")
    @Expose
    private String endAddress;

    @SerializedName("end_location")
    @Expose
    private Bound endLocation;

    @SerializedName("start_address")
    @Expose
    private String startAddress;

    @SerializedName("start_location")
    @Expose
    private Bound startLocation;

    @SerializedName("steps")
    @Expose
    private List<Step> steps = null;

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

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public Bound getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Bound endLocation) {
        this.endLocation = endLocation;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public Bound getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Bound startLocation) {
        this.startLocation = startLocation;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }
}
