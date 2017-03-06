package com.ede.standyourground.app.service.impl;

import android.graphics.Color;

import com.ede.standyourground.app.model.Route;
import com.ede.standyourground.app.model.Routes;
import com.ede.standyourground.app.service.api.DirectionsService;
import com.ede.standyourground.app.service.api.DrawRouteService;
import com.ede.standyourground.framework.Logger;
import com.ede.standyourground.framework.dagger.providers.GoogleMapProvider;
import com.ede.standyourground.game.framework.management.impl.WorldManager;
import com.ede.standyourground.game.model.Units;
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
    private final Lazy<WorldManager> worldManager;

    @Inject
    DrawRouteServiceImpl(Lazy<GoogleMapProvider> googleMapProvider,
                         Lazy<DirectionsService> directionsService,
                         Lazy<WorldManager> worldManager) {
        this.googleMapProvider = googleMapProvider;
        this.directionsService = directionsService;
        this.worldManager = worldManager;
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

                worldManager.get().createPlayerUnit(polyline.getPoints(), playerLocation, toCreate);
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

        directionsService.get().getRoutes(playerLocation, opponentLocation, waypointsLatLng, new Callback<Routes>() {
            @Override
            public void onResponse(Call<Routes> call, Response<Routes> response) {
                logger.i("response with routes received");
                Polyline polyline = drawRoute(response.body().getRoutes().get(0));

                worldManager.get().createEnemyUnit(polyline.getPoints(), playerLocation, toCreate);
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
