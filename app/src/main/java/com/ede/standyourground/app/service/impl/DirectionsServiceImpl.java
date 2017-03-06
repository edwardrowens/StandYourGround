package com.ede.standyourground.app.service.impl;

import com.ede.standyourground.app.api.DirectionsApi;
import com.ede.standyourground.app.model.Routes;
import com.ede.standyourground.app.service.android.ServiceGenerator;
import com.ede.standyourground.app.service.api.DirectionsService;
import com.ede.standyourground.app.to.RoutesRequestTO;
import com.ede.standyourground.framework.api.RouteService;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DirectionsServiceImpl implements DirectionsService {

    private final Lazy<RouteService> routeService;

    @Inject
    DirectionsServiceImpl(Lazy<RouteService> routeService) {
        this.routeService = routeService;
    }

    @Override
    public void getRoutes(final LatLng origin, final LatLng destination, final List<LatLng> waypoints, final Callback<Routes> callback) {
        DirectionsApi directionsApi = ServiceGenerator.createService(DirectionsApi.class);

        RoutesRequestTO routesRequestTO = new RoutesRequestTO();
        routesRequestTO.setEndLat(destination.latitude);
        routesRequestTO.setEndLng(destination.longitude);
        routesRequestTO.setStartLat(origin.latitude);
        routesRequestTO.setStartLng(origin.longitude);
        routesRequestTO.setWaypoints(waypoints);

        Call<Routes> routesCall = directionsApi.calculateRoutes(routesRequestTO);

        routesCall.enqueue(new Callback<Routes>() {
            @Override
            public void onResponse(Call<Routes> call, Response<Routes> response) {
                response.body().setTotalDistance(routeService.get().getTotalDistance(response.body()));
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<Routes> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }
}
