package com.ede.standyourground.app.service.api;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public interface OnMapReadyService {
    void onMapReady(final GoogleMap googleMap, final LatLng opponentLocation, final LatLng playerLocation);
}
