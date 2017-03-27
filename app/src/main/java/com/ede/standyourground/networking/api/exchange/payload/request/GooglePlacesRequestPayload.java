package com.ede.standyourground.networking.api.exchange.payload.request;

import com.ede.standyourground.networking.api.model.GooglePlacesType;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 */

public class GooglePlacesRequestPayload {

    @Expose
    @SerializedName("type")
    private String type;

    @Expose
    @SerializedName("location")
    private LatLng location;

    @Expose
    @SerializedName("radius")
    private int radius;


    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public String getType() {
        return type;
    }

    public void setType(GooglePlacesType type) {
        this.type = type.toString();
    }
}
