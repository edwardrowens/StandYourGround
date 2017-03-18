package com.ede.standyourground.app.service.api;

import com.google.android.gms.maps.model.LatLng;

public interface MapSetupService {
    void setupMap(final LatLng opponentLocation, final LatLng playerLocation);
}
