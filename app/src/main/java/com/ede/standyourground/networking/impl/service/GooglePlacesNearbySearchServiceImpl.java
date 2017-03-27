package com.ede.standyourground.networking.impl.service;

import com.ede.standyourground.framework.api.service.LatLngService;
import com.ede.standyourground.networking.api.exchange.api.GooglePlacesApi;
import com.ede.standyourground.networking.api.exchange.payload.request.GooglePlacesRequestPayload;
import com.ede.standyourground.networking.api.exchange.payload.response.GooglePlacesResponsePayload;
import com.ede.standyourground.networking.api.model.GooglePlacesType;
import com.ede.standyourground.networking.api.service.GooglePlacesNearbySearchService;
import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;

import dagger.Lazy;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 *
 */

public class GooglePlacesNearbySearchServiceImpl implements GooglePlacesNearbySearchService {

    private final Lazy<Retrofit> retrofit;
    private final Lazy<LatLngService> latLngService;

    @Inject
    GooglePlacesNearbySearchServiceImpl(Lazy<Retrofit> retrofit,
                                        Lazy<LatLngService> latLngService) {
        this.retrofit = retrofit;
        this.latLngService = latLngService;
    }

    @Override
    public void nearbySearch(LatLng playerLocation, LatLng opponentLocation, final Callback<GooglePlacesResponsePayload> callback) {

        final int radius = (int)latLngService.get().calculateDistance(playerLocation, opponentLocation);
        final LatLng midpoint = latLngService.get().midpoint(playerLocation, opponentLocation);

        GooglePlacesRequestPayload googlePlacesRequestPayload = new GooglePlacesRequestPayload();
        googlePlacesRequestPayload.setRadius(radius);
        googlePlacesRequestPayload.setType(GooglePlacesType.PHARMACY);
        googlePlacesRequestPayload.setLocation(midpoint);

        GooglePlacesApi googlePlacesApi = retrofit.get().create(GooglePlacesApi.class);
        Call<GooglePlacesResponsePayload> googlePlacesApiCall = googlePlacesApi.retrieveNearbyPlaces(googlePlacesRequestPayload);
        googlePlacesApiCall.enqueue(new Callback<GooglePlacesResponsePayload>() {
            @Override
            public void onResponse(Call<GooglePlacesResponsePayload> call, Response<GooglePlacesResponsePayload> response) {
                callback.onResponse(call, response);
            }
            @Override
            public void onFailure(Call<GooglePlacesResponsePayload> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }
}
