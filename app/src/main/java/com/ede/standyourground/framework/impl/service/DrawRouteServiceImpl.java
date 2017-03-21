package com.ede.standyourground.framework.impl.service;

import android.graphics.Color;

import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.framework.api.dagger.providers.GoogleMapProvider;
import com.ede.standyourground.framework.api.service.DirectionsService;
import com.ede.standyourground.framework.api.service.DrawRouteService;
import com.ede.standyourground.game.api.model.Units;
import com.ede.standyourground.game.api.service.UnitService;
import com.ede.standyourground.networking.api.model.Route;
import com.ede.standyourground.networking.api.model.Routes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
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
    private final Lazy<GoogleMapProvider> googleMapProvider;
    private final Lazy<DirectionsService> directionsService;
    private final Lazy<UnitService> unitService;

    @Inject
    DrawRouteServiceImpl(Lazy<GoogleMapProvider> googleMapProvider,
                         Lazy<DirectionsService> directionsService,
                         Lazy<UnitService> unitService) {
        this.googleMapProvider = googleMapProvider;
        this.directionsService = directionsService;
        this.unitService = unitService;
    }

    @Override
    public void drawRoutesForUnit(final Units toCreate, final List<Marker> markers, final LatLng playerLocation, final LatLng opponentLocation) {
        ArrayList<LatLng> waypointsLatLng = new ArrayList<>();
        for (Marker marker : markers) {
            waypointsLatLng.add(marker.getPosition());
        }

        directionsService.get().getRoutes(playerLocation, opponentLocation, waypointsLatLng, new Callback<Routes>() {
            @Override
            public void onResponse(Call<Routes> call, Response<Routes> response) {
                logger.i("response with routes received");
                Polyline polyline = drawRoute(response.body().getRoutes().get(0));

                unitService.get().createPlayerUnit(polyline.getPoints(), playerLocation, toCreate);
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
    public void drawRoutesForEnemyUnit(final Units toCreate, final List<Marker> markers, final LatLng playerLocation, final LatLng opponentLocation) {
        ArrayList<LatLng> waypointsLatLng = new ArrayList<>();
        for (Marker marker : markers) {
            waypointsLatLng.add(marker.getPosition());
        }

        directionsService.get().getRoutes(opponentLocation, playerLocation, waypointsLatLng, new Callback<Routes>() {
            @Override
            public void onResponse(Call<Routes> call, Response<Routes> response) {
                logger.i("response with routes received");
                Polyline polyline = drawRoute(response.body().getRoutes().get(0));

                unitService.get().createEnemyUnit(polyline.getPoints(), opponentLocation, toCreate);
            }

            @Override
            public void onFailure(Call<Routes> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private Polyline drawRoute(Route route) {
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions = polylineOptions.addAll(PolyUtil.decode(route.getOverviewPolyline().getPoints()))
                .width(20)
                .color(Color.BLACK);
        logger.i("drawing route");
        return googleMapProvider.get().getGoogleMap().addPolyline(polylineOptions);
    }
}