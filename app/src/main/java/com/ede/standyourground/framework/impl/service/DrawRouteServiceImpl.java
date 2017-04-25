package com.ede.standyourground.framework.impl.service;

import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.framework.api.service.DirectionsService;
import com.ede.standyourground.framework.api.service.DrawRouteService;
import com.ede.standyourground.game.api.model.UnitType;
import com.ede.standyourground.game.api.service.UnitService;
import com.ede.standyourground.networking.api.model.Routes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DrawRouteServiceImpl implements DrawRouteService {

    private static final Logger logger = new Logger(DrawRouteServiceImpl.class);

    private final Lazy<DirectionsService> directionsService;
    private final Lazy<UnitService> unitService;

    @Inject
    DrawRouteServiceImpl(Lazy<DirectionsService> directionsService,
                         Lazy<UnitService> unitService) {
        this.directionsService = directionsService;
        this.unitService = unitService;
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

    /**
     * TODO DELETE ME
     */
    @Override
    public void createRoutesForEnemyUnit(final UnitType toCreate, final List<Marker> markers, final LatLng playerLocation, final LatLng opponentLocation) {
        ArrayList<LatLng> waypointsLatLng = new ArrayList<>();
        for (Marker marker : markers) {
            waypointsLatLng.add(marker.getPosition());
        }

        directionsService.get().getRoutes(opponentLocation, playerLocation, waypointsLatLng, new Callback<Routes>() {
            @Override
            public void onResponse(Call<Routes> call, Response<Routes> response) {
                logger.i("response with routes received");
                unitService.get().createEnemyUnit(PolyUtil.decode(response.body().getRoutes().get(0).getOverviewPolyline().getPoints()), opponentLocation, toCreate);
            }

            @Override
            public void onFailure(Call<Routes> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
