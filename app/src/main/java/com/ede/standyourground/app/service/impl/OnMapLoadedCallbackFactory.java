package com.ede.standyourground.app.service.impl;

import com.ede.standyourground.R;
import com.ede.standyourground.app.ui.UnitGroupComponent;
import com.ede.standyourground.framework.api.LatLngService;
import com.ede.standyourground.framework.dagger.providers.GoogleMapProvider;
import com.ede.standyourground.game.framework.management.api.UnitService;
import com.ede.standyourground.game.model.Units;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import javax.inject.Inject;

import dagger.Lazy;


public class OnMapLoadedCallbackFactory {

    private static final int CAMERA_PADDING = 200;

    private final Lazy<UnitService> unitService;
    private final Lazy<LatLngService> latLngService;
    private final Lazy<GoogleMapProvider> googleMapProvider;

    @Inject
    OnMapLoadedCallbackFactory(Lazy<UnitService> unitService,
                               Lazy<LatLngService> latLngService,
                               Lazy<GoogleMapProvider> googleMapProvider) {
        this.unitService = unitService;
        this.latLngService = latLngService;
        this.googleMapProvider = googleMapProvider;
    }

    public GoogleMap.OnMapLoadedCallback createOnMapLoadedCallback(final LatLng playerLocation, final LatLng opponentLocation, final UnitGroupComponent unitGroupComponent) {
        return new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                unitGroupComponent.initialize(R.id.mapContainer);

                // Set up the camera's initial position
                LatLngBounds latLngBounds = LatLngBounds.builder().include(opponentLocation).include(playerLocation).build();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(latLngBounds, CAMERA_PADDING);
                googleMapProvider.get().getGoogleMap().animateCamera(cameraUpdate, new GoogleMap.CancelableCallback() {
                    @Override
                    public void onFinish() {
                        float zoom = googleMapProvider.get().getGoogleMap().getCameraPosition().zoom;
                        CameraPosition cameraPosition = CameraPosition.builder()
                                .target(latLngService.get().midpoint(playerLocation, opponentLocation))
                                .bearing((float)latLngService.get().bearing(playerLocation, opponentLocation))
                                .zoom(zoom)
                                .build();
                        googleMapProvider.get().getGoogleMap().animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), new GoogleMap.CancelableCallback() {
                            @Override
                            public void onFinish() {
                                googleMapProvider.get().getGoogleMap().getUiSettings().setRotateGesturesEnabled(false);
                                unitService.get().createPlayerUnit(null, playerLocation, Units.BASE);

                                // TODO DELETE
                                unitService.get().createEnemyUnit(null, opponentLocation, Units.BASE);
                                // TODO END OF DELETE
                            }

                            @Override
                            public void onCancel() {
                            }
                        });
                    }

                    @Override
                    public void onCancel() {
                    }
                });
            }
        };
    }
}
