package com.ede.standyourground.networking.api.service;

import com.ede.standyourground.networking.api.exchange.payload.response.GooglePlacesResponsePayload;
import com.google.android.gms.maps.model.LatLng;

import retrofit2.Callback;

/**
 *
 */

public interface GooglePlacesNearbySearchService {
    void nearbySearch(LatLng playerLocation, LatLng opponentLocation, Callback<GooglePlacesResponsePayload> callback);
}
