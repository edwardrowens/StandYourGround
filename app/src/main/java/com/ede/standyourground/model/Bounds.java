package com.ede.standyourground.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Eddie on 1/30/2017.
 */

public class Bounds {

    @SerializedName("northeast")
    @Expose
    private Bound northEast;

    @SerializedName("southwest")
    @Expose
    private Bound southWest;

    public Bound getNorthEast() {
        return northEast;
    }

    public void setNorthEast(Bound northEast) {
        this.northEast = northEast;
    }

    public Bound getSouthWest() {
        return southWest;
    }

    public void setSouthWest(Bound southWest) {
        this.southWest = southWest;
    }
}
