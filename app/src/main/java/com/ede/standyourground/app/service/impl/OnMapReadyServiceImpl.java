package com.ede.standyourground.app.service.impl;

import com.ede.standyourground.app.service.api.OnMapReadyService;
import com.ede.standyourground.framework.api.LatLngService;
import com.ede.standyourground.framework.dagger.providers.GoogleMapProvider;
import com.ede.standyourground.game.framework.management.impl.WorldManager;
import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;

import dagger.Lazy;

public class OnMapReadyServiceImpl implements OnMapReadyService {

    private static final int CAMERA_PADDING = 200;

    private final Lazy<WorldManager> worldManager;
    private final Lazy<LatLngService> latLngService;
    private final Lazy<GoogleMapProvider> googleMapProvider;

    @Inject
    OnMapReadyServiceImpl(Lazy<WorldManager> worldManager,
                          Lazy<LatLngService> latLngService,
                          Lazy<GoogleMapProvider> googleMapProvider) {
        this.worldManager = worldManager;
        this.latLngService = latLngService;
        this.googleMapProvider = googleMapProvider;
    }

    @Override
    public void onMapReady(final LatLng opponentLocation, final LatLng playerLocation) {
        worldManager.get().start();

        // Updating google map settings
        googleMapProvider.get().getGoogleMap().getUiSettings().setMapToolbarEnabled(false);
        googleMapProvider.get().getGoogleMap().getUiSettings().setCompassEnabled(false);

        OnMapLoadedCallback onMapLoadedCallback = new OnMapLoadedCallback(worldManager, latLngService, googleMapProvider, opponentLocation, playerLocation);

        googleMapProvider.get().getGoogleMap().setOnMapLoadedCallback(onMapLoadedCallback);
    }
}
