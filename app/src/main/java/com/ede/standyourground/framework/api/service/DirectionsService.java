package com.ede.standyourground.framework.api.service;

import com.ede.standyourground.networking.api.model.Routes;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import retrofit2.Callback;


public interface DirectionsService {
    void getRoutes(LatLng origin, LatLng destination, List<LatLng> intermediaryPositions, final Callback<Routes> callback);
}
