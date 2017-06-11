package com.ede.standyourground.app.event;

import android.widget.ImageButton;

import com.ede.standyourground.R;
import com.ede.standyourground.app.ui.impl.component.NeutralCampListingComponent;
import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.framework.api.dagger.providers.GoogleMapProvider;
import com.ede.standyourground.framework.api.service.LatLngService;
import com.ede.standyourground.game.api.model.GameMode;
import com.ede.standyourground.game.api.service.GameService;
import com.ede.standyourground.game.api.service.NeutralCampService;
import com.ede.standyourground.networking.api.event.GooglePlacesResponseCallback;
import com.ede.standyourground.networking.api.model.GooglePlaceResult;
import com.ede.standyourground.networking.api.service.GooglePlacesNearbySearchService;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;


public class OnMapLoadedCallbackFactory {

    private static final Logger logger = new Logger(OnMapLoadedCallbackFactory.class);

    private static final int CAMERA_PADDING = 50;

    private final Lazy<LatLngService> latLngService;
    private final Lazy<GoogleMapProvider> googleMapProvider;
    private final Lazy<GooglePlacesNearbySearchService> googlePlacesNearbySearchService;
    private final Lazy<NeutralCampService> neutralCampService;
    private final Lazy<GameService> gameService;

    @Inject
    OnMapLoadedCallbackFactory(Lazy<LatLngService> latLngService,
                               Lazy<GoogleMapProvider> googleMapProvider,
                               Lazy<GooglePlacesNearbySearchService> googlePlacesNearbySearchService,
                               Lazy<NeutralCampService> neutralCampService,
                               Lazy<GameService> gameService) {
        this.latLngService = latLngService;
        this.googleMapProvider = googleMapProvider;
        this.googlePlacesNearbySearchService = googlePlacesNearbySearchService;
        this.neutralCampService = neutralCampService;
        this.gameService = gameService;
    }

    public GoogleMap.OnMapLoadedCallback createOnMapLoadedCallback(final GameMode gameMode, final LatLng playerLocation, final LatLng opponentLocation, final NeutralCampListingComponent neutralCampListingComponent, final ImageButton unitSelectorButton, final ImageButton resetLocationButton, final int cameraPaddingInPixels) {
        return new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                gameService.get().startGame(gameMode, playerLocation, opponentLocation);
                neutralCampListingComponent.initialize(R.id.mapContainer);

                // Set up the camera's initial position
                final LatLngBounds latLngBounds = LatLngBounds.builder().include(opponentLocation).include(playerLocation).build();
                final CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(latLngBounds, cameraPaddingInPixels);

                googlePlacesNearbySearchService.get().nearbySearch(playerLocation, opponentLocation, new GooglePlacesResponseCallback() {
                    @Override
                    public void onResponse(List<GooglePlaceResult> results, int statusCode) {
                        if (statusCode != 200) {
                            logger.e("Received response code %d. Could not initialize neutral camps.", statusCode);
                        } else {
                            List<GooglePlaceResult> filteredGooglePlaceResults = neutralCampService.get().filterNeutralCamps(results, playerLocation, opponentLocation);
                            neutralCampService.get().createNeutralCamps(filteredGooglePlaceResults);
                        }
                        animateCamera(cameraUpdate, playerLocation, opponentLocation, unitSelectorButton, resetLocationButton);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        logger.e("Could not initialize the neutral camps", t);
                        animateCamera(cameraUpdate, playerLocation, opponentLocation, unitSelectorButton, resetLocationButton);
                    }
                });
            }
        };
    }

    private void animateCamera(CameraUpdate cameraUpdate, final LatLng playerLocation, final LatLng opponentLocation, final ImageButton unitSelectorButton, final ImageButton resetLocationButton) {
        googleMapProvider.get().getGoogleMap().animateCamera(cameraUpdate, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                float zoom = googleMapProvider.get().getGoogleMap().getCameraPosition().zoom;
                CameraPosition cameraPosition = CameraPosition.builder()
                        .target(latLngService.get().midpoint(playerLocation, opponentLocation))
                        .bearing((float) latLngService.get().bearing(playerLocation, opponentLocation))
                        .zoom(zoom)
                        .build();
                googleMapProvider.get().getGoogleMap().animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), new GoogleMap.CancelableCallback() {
                    @Override
                    public void onFinish() {
                        googleMapProvider.get().getGoogleMap().getUiSettings().setRotateGesturesEnabled(false);
                        googleMapProvider.get().getGoogleMap().getUiSettings().setAllGesturesEnabled(true);
                        unitSelectorButton.setEnabled(true);
                        resetLocationButton.setEnabled(true);
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
