package com.ede.standyourground.networking.api.model;

import com.google.android.gms.maps.model.LatLng;

/**
 *
 */

public class GooglePlaceGeometry {
    private LatLng location;

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }
}
