package com.ede.standyourground.app.event;

import android.widget.ImageButton;

import com.ede.standyourground.R;
import com.ede.standyourground.app.ui.impl.component.NeutralCampListingComponent;
import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.framework.api.dagger.providers.GoogleMapProvider;
import com.ede.standyourground.framework.api.service.CameraService;
import com.ede.standyourground.game.api.model.GameMode;
import com.ede.standyourground.game.api.service.GameService;
import com.ede.standyourground.game.api.service.NeutralCampService;
import com.ede.standyourground.networking.api.event.GooglePlacesResponseCallback;
import com.ede.standyourground.networking.api.model.GooglePlaceResult;
import com.ede.standyourground.networking.api.service.GooglePlacesNearbySearchService;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

import dagger.Lazy;


public class OnMapLoadedCallbackFactory {

    private static final Logger logger = new Logger(OnMapLoadedCallbackFactory.class);

    private final Lazy<GoogleMapProvider> googleMapProvider;
    private final Lazy<GooglePlacesNearbySearchService> googlePlacesNearbySearchService;
    private final Lazy<NeutralCampService> neutralCampService;
    private final Lazy<GameService> gameService;
    private final Lazy<CameraService> cameraService;

    @Inject
    OnMapLoadedCallbackFactory(Lazy<GoogleMapProvider> googleMapProvider,
                               Lazy<GooglePlacesNearbySearchService> googlePlacesNearbySearchService,
                               Lazy<NeutralCampService> neutralCampService,
                               Lazy<GameService> gameService,
                               Lazy<CameraService> cameraService) {
        this.googleMapProvider = googleMapProvider;
        this.googlePlacesNearbySearchService = googlePlacesNearbySearchService;
        this.neutralCampService = neutralCampService;
        this.gameService = gameService;
        this.cameraService = cameraService;
    }

    public GoogleMap.OnMapLoadedCallback createOnMapLoadedCallback(final GameMode gameMode, final LatLng playerLocation, final LatLng opponentLocation, final NeutralCampListingComponent neutralCampListingComponent, final ImageButton unitSelectorButton, final ImageButton resetLocationButton, final AtomicReference<CameraPosition> cameraPositionReference) {
        return new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                gameService.get().startGame(gameMode, playerLocation, opponentLocation);
                neutralCampListingComponent.initialize(R.id.mapContainer);

                googlePlacesNearbySearchService.get().nearbySearch(playerLocation, opponentLocation, new GooglePlacesResponseCallback() {
                    @Override
                    public void onResponse(List<GooglePlaceResult> results, int statusCode) {
                        if (statusCode != 200) {
                            logger.e("Received response code %d. Could not initialize neutral camps.", statusCode);
                        } else {
                            List<GooglePlaceResult> filteredGooglePlaceResults = neutralCampService.get().filterNeutralCamps(results, playerLocation, opponentLocation);
                            neutralCampService.get().createNeutralCamps(filteredGooglePlaceResults);
                        }
                        CameraPosition cameraPosition = cameraService.get().focusCamera(playerLocation, opponentLocation, new CameraAnimationCallback() {
                            @Override
                            public void onAnimated() {
                                googleMapProvider.get().getGoogleMap().getUiSettings().setRotateGesturesEnabled(false);
                                googleMapProvider.get().getGoogleMap().getUiSettings().setAllGesturesEnabled(true);
                                unitSelectorButton.setEnabled(true);
                                resetLocationButton.setEnabled(true);
                            }
                        });
                        cameraPositionReference.set(cameraPosition);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        logger.e("Could not initialize the neutral camps", t);
                        CameraPosition cameraPosition = cameraService.get().focusCamera(playerLocation, opponentLocation, new CameraAnimationCallback() {
                            @Override
                            public void onAnimated() {
                                googleMapProvider.get().getGoogleMap().getUiSettings().setRotateGesturesEnabled(false);
                                googleMapProvider.get().getGoogleMap().getUiSettings().setAllGesturesEnabled(true);
                                unitSelectorButton.setEnabled(true);
                                resetLocationButton.setEnabled(true);
                            }
                        });
                        cameraPositionReference.set(cameraPosition);
                    }
                });
            }
        };
    }
}
