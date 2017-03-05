package com.ede.standyourground.app.service.impl;

import com.ede.standyourground.app.service.api.OnMapReadyService;
import com.ede.standyourground.framework.api.LatLngService;
import com.ede.standyourground.game.framework.management.impl.WorldManager;
import com.ede.standyourground.game.model.Units;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

import dagger.Lazy;

public class OnMapReadyServiceImpl implements OnMapReadyService {

    private static final int CAMERA_PADDING = 200;

    private final Lazy<WorldManager> worldManager;
    private final Lazy<LatLngService> latLngService;

    @Inject
    OnMapReadyServiceImpl(Lazy<WorldManager> worldManager,
                          Lazy<LatLngService> latLngService) {
        this.worldManager = worldManager;
        this.latLngService = latLngService;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap, final LatLng opponentLocation, final LatLng playerLocation) {
        worldManager.get().start();

        // Updating google map settings
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setCompassEnabled(false);

        // Add markers representing the opponent's and the player's location
        googleMap.addMarker(new MarkerOptions().position(opponentLocation));
        googleMap.addMarker(new MarkerOptions().position(playerLocation));

        // Set up the camera's initial position
        LatLngBounds latLngBounds = LatLngBounds.builder().include(opponentLocation).include(playerLocation).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(latLngBounds, CAMERA_PADDING);
        googleMap.animateCamera(cameraUpdate, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                float zoom = googleMap.getCameraPosition().zoom;
                CameraPosition cameraPosition = CameraPosition.builder()
                        .target(latLngService.get().midpoint(playerLocation, opponentLocation))
                        .bearing(latLngService.get().bearing(playerLocation, opponentLocation))
                        .zoom(zoom)
                        .build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), new GoogleMap.CancelableCallback() {
                    @Override
                    public void onFinish() {
                        googleMap.getUiSettings().setRotateGesturesEnabled(false);
                        worldManager.get().createPlayerUnit(null, playerLocation, Units.BASE);

                        // TODO DELETE
                        worldManager.get().createEnemyUnit(null, opponentLocation, Units.BASE);
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
}
