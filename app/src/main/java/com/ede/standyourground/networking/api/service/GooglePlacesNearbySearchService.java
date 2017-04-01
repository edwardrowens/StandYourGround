package com.ede.standyourground.networking.api.service;

import com.ede.standyourground.networking.api.event.GooglePlacesResponseCallback;
import com.google.android.gms.maps.model.LatLng;

/**
 *
 */

public interface GooglePlacesNearbySearchService {
    void nearbySearch(LatLng playerLocation, LatLng opponentLocation, GooglePlacesResponseCallback callback);
}
