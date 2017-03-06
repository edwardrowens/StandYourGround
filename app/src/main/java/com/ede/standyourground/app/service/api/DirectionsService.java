package com.ede.standyourground.app.service.api;

import com.ede.standyourground.app.model.Routes;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import retrofit2.Callback;


public interface DirectionsService {
    void getRoutes(LatLng origin, LatLng destination, List<LatLng> waypoints, final Callback<Routes> callback);
}
