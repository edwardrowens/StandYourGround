package com.ede.standyourground.app.service.impl;

import com.ede.standyourground.app.activity.MapsActivity;
import com.ede.standyourground.app.service.api.MapSetupService;
import com.ede.standyourground.app.ui.UnitGroupComponent;
import com.ede.standyourground.framework.dagger.providers.GoogleMapProvider;
import com.ede.standyourground.game.model.api.OnUnitClickListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;

import dagger.Lazy;

public class MapSetupServiceImpl implements MapSetupService {

    private final Lazy<GoogleMapProvider> googleMapProvider;
    private final Lazy<OnUnitClickListener> onUnitClickListener;
    private final Lazy<OnMapLoadedCallbackFactory> onMapLoadedCallbackFactory;

    @Inject
    MapSetupServiceImpl(Lazy<GoogleMapProvider> googleMapProvider,
                        Lazy<OnUnitClickListener> onUnitClickListener,
                        Lazy<OnMapLoadedCallbackFactory> onMapLoadedCallbackFactory) {
        this.googleMapProvider = googleMapProvider;
        this.onUnitClickListener = onUnitClickListener;
        this.onMapLoadedCallbackFactory = onMapLoadedCallbackFactory;
    }

    @Override
    public void setupMap(final LatLng opponentLocation, final LatLng playerLocation) {
        googleMapProvider.get().getGoogleMap().setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                ((UnitGroupComponent) MapsActivity.getComponent(UnitGroupComponent.class)).clear();
            }
        });

        // Updating google map settings
        googleMapProvider.get().getGoogleMap().getUiSettings().setMapToolbarEnabled(false);
        googleMapProvider.get().getGoogleMap().getUiSettings().setCompassEnabled(false);

        googleMapProvider.get().getGoogleMap().setOnMapLoadedCallback(onMapLoadedCallbackFactory.get().createOnMapLoadedCallback(playerLocation, opponentLocation));
        googleMapProvider.get().getGoogleMap().setOnCircleClickListener(onUnitClickListener.get());
    }
}
