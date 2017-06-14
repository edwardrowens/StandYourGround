package com.ede.standyourground.framework.impl.service;

import com.ede.standyourground.app.event.CameraAnimationCallback;
import com.ede.standyourground.framework.api.dagger.providers.GoogleMapProvider;
import com.ede.standyourground.framework.api.service.CameraService;
import com.ede.standyourground.framework.api.service.LatLngService;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import javax.inject.Inject;

import dagger.Lazy;

/**
 *
 */
public class CameraServiceImpl implements CameraService {

    private final Lazy<GoogleMapProvider> googleMapProvider;
    private final Lazy<LatLngService> latLngService;

    @Inject
    public CameraServiceImpl(Lazy<GoogleMapProvider> googleMapProvider,
                             Lazy<LatLngService> latLngService) {
        this.googleMapProvider = googleMapProvider;
        this.latLngService = latLngService;
    }

    @Override
    public CameraPosition focusCamera(LatLng p1, LatLng p2, final CameraAnimationCallback cameraAnimationCallback) {
        // Set up the camera's initial position
        final LatLngBounds latLngBounds = LatLngBounds.builder().include(p2).include(p1).build();
        final CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(latLngBounds, 0);
        googleMapProvider.get().getGoogleMap().moveCamera(cameraUpdate);

        float zoom = googleMapProvider.get().getGoogleMap().getCameraPosition().zoom - .5f;
        CameraPosition cameraPosition = CameraPosition.builder()
                .target(latLngService.get().midpoint(p1, p2))
                .bearing((float) latLngService.get().bearing(p1, p2))
                .zoom(zoom)
                .build();
        googleMapProvider.get().getGoogleMap().animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                if (cameraAnimationCallback != null) {
                    cameraAnimationCallback.onAnimated();
                }
            }

            @Override
            public void onCancel() {
            }
        });

        return cameraPosition;
    }
}
