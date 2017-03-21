package com.ede.standyourground.framework.impl.service;

import com.ede.standyourground.framework.api.service.DirectionsService;
import com.ede.standyourground.framework.api.service.RouteService;
import com.ede.standyourground.networking.api.exchange.api.GoogleDirectionsApi;
import com.ede.standyourground.networking.api.exchange.payload.request.RoutesRequest;
import com.ede.standyourground.networking.api.model.Routes;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class DirectionsServiceImpl implements DirectionsService {

    private final Lazy<RouteService> routeService;
    private final Lazy<Retrofit> retrofit;

    @Inject
    DirectionsServiceImpl(Lazy<RouteService> routeService,
                          Lazy<Retrofit> retrofit) {
        this.routeService = routeService;
        this.retrofit = retrofit;
    }

    @Override
    public void getRoutes(final LatLng origin, final LatLng destination, final List<LatLng> waypoints, final Callback<Routes> callback) {
        GoogleDirectionsApi googleDirectionsApi = retrofit.get().create(GoogleDirectionsApi.class);

        RoutesRequest routesRequest = new RoutesRequest();
        routesRequest.setEndLat(destination.latitude);
        routesRequest.setEndLng(destination.longitude);
        routesRequest.setStartLat(origin.latitude);
        routesRequest.setStartLng(origin.longitude);
        routesRequest.setWaypoints(waypoints);

        Call<Routes> routesCall = googleDirectionsApi.calculateRoutes(routesRequest);

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
