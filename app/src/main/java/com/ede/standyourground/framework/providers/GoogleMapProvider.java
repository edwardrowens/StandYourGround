package com.ede.standyourground.framework.providers;

import com.google.android.gms.maps.GoogleMap;

public class GoogleMapProvider {
    private GoogleMap googleMap;

    public GoogleMap getGoogleMap() {
        return googleMap;
    }

    public void setGoogleMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }
}
