package com.ede.standyourground.framework.impl.service;

import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.framework.api.service.DirectionsService;
import com.ede.standyourground.framework.api.service.DrawRouteService;
import com.ede.standyourground.networking.api.model.Routes;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DrawRouteServiceImpl implements DrawRouteService {

    private static final Logger logger = new Logger(DrawRouteServiceImpl.class);

    private final Lazy<DirectionsService> directionsService;

    @Inject
    DrawRouteServiceImpl(Lazy<DirectionsService> directionsService) {
        this.directionsService = directionsService;
    }

    @Override
    public void createRoutesForUnit(final LatLng start, final LatLng end, final List<LatLng> intermediaryPoints, final Callback<Routes> callback) {
        directionsService.get().getRoutes(start, end, intermediaryPoints, new Callback<Routes>() {
            @Override
            public void onResponse(Call<Routes> call, Response<Routes> response) {
                logger.i("response with routes received");
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<Routes> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
