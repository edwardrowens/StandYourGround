package com.ede.standyourground.app.service.api;

import com.google.android.gms.maps.model.LatLng;

public interface OnMapReadyService {
    void onMapReady(final LatLng opponentLocation, final LatLng playerLocation);
}
