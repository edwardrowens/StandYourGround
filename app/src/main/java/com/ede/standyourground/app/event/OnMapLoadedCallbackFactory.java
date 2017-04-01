package com.ede.standyourground.app.event;

import com.ede.standyourground.R;
import com.ede.standyourground.app.ui.impl.component.NeutralCampListingComponent;
import com.ede.standyourground.app.ui.impl.component.UnitGroupComponent;
import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.framework.api.dagger.providers.GoogleMapProvider;
import com.ede.standyourground.framework.api.service.LatLngService;
import com.ede.standyourground.game.api.model.Units;
import com.ede.standyourground.game.api.service.NeutralCampService;
import com.ede.standyourground.game.api.service.UnitService;
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

    private final Lazy<UnitService> unitService;
    private final Lazy<LatLngService> latLngService;
    private final Lazy<GoogleMapProvider> googleMapProvider;
    private final Lazy<GooglePlacesNearbySearchService> googlePlacesNearbySearchService;
    private final Lazy<NeutralCampService> neutralCampService;

    @Inject
    OnMapLoadedCallbackFactory(Lazy<UnitService> unitService,
                               Lazy<LatLngService> latLngService,
                               Lazy<GoogleMapProvider> googleMapProvider,
                               Lazy<GooglePlacesNearbySearchService> googlePlacesNearbySearchService,
                               Lazy<NeutralCampService> neutralCampService) {
        this.unitService = unitService;
        this.latLngService = latLngService;
        this.googleMapProvider = googleMapProvider;
        this.googlePlacesNearbySearchService = googlePlacesNearbySearchService;
        this.neutralCampService = neutralCampService;
    }

    public GoogleMap.OnMapLoadedCallback createOnMapLoadedCallback(final LatLng playerLocation, final LatLng opponentLocation, final UnitGroupComponent unitGroupComponent, final NeutralCampListingComponent neutralCampListingComponent) {
        return new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                unitGroupComponent.initialize(R.id.mapContainer);
                neutralCampListingComponent.initialize(R.id.mapContainer);

                // Set up the camera's initial position
                final LatLngBounds latLngBounds = LatLngBounds.builder().include(opponentLocation).include(playerLocation).build();
                final CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(latLngBounds, CAMERA_PADDING);

                googlePlacesNearbySearchService.get().nearbySearch(playerLocation, opponentLocation, new GooglePlacesResponseCallback() {
                    @Override
                    public void onResponse(List<GooglePlaceResult> results, int statusCode) {
                        if (statusCode != 200) {
                            logger.e("Received response code %d. Could not initialize neutral camps.", statusCode);
                        } else {
                            List<GooglePlaceResult> filteredGooglePlaceResults = neutralCampService.get().filterNeutralCamps(results, playerLocation, opponentLocation);
                            neutralCampService.get().createNeutralCamps(filteredGooglePlaceResults);
                        }
                        animateCamera(cameraUpdate, playerLocation, opponentLocation);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        logger.e("Could not initialize the neutral camps", t);
                        animateCamera(cameraUpdate, playerLocation, opponentLocation);
                    }
                });
            }
        };
    }

    private void animateCamera(CameraUpdate cameraUpdate, final LatLng playerLocation, final LatLng opponentLocation) {
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
                        unitService.get().createFriendlyUnit(null, playerLocation, Units.BASE);
                        unitService.get().createEnemyUnit(null, opponentLocation, Units.BASE);
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
