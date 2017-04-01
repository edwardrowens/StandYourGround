package com.ede.standyourground.networking.impl.service;

import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.framework.api.service.LatLngService;
import com.ede.standyourground.networking.api.event.GooglePlacesResponseCallback;
import com.ede.standyourground.networking.api.exchange.api.GooglePlacesApi;
import com.ede.standyourground.networking.api.exchange.payload.request.GooglePlacesRequestPayload;
import com.ede.standyourground.networking.api.exchange.payload.response.GooglePlacesResponsePayload;
import com.ede.standyourground.networking.api.model.GooglePlaceResult;
import com.ede.standyourground.networking.api.model.GooglePlacesType;
import com.ede.standyourground.networking.api.service.GooglePlacesNearbySearchService;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

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

    private final Logger logger = new Logger(GooglePlacesServiceImpl.class);

    private final Lazy<Retrofit> retrofit;
    private final Lazy<LatLngService> latLngService;

    @Inject
    GooglePlacesNearbySearchServiceImpl(Lazy<Retrofit> retrofit,
                                        Lazy<LatLngService> latLngService) {
        this.retrofit = retrofit;
        this.latLngService = latLngService;
    }

    @Override
    public void nearbySearch(LatLng playerLocation, LatLng opponentLocation, final GooglePlacesResponseCallback callback) {

        final int radius = (int) latLngService.get().calculateDistance(playerLocation, opponentLocation);
        final LatLng midpoint = latLngService.get().midpoint(playerLocation, opponentLocation);

        final List<GooglePlaceResult> results = new ArrayList<>();
        final List<GooglePlacesType> types = new ArrayList<>();
        types.add(GooglePlacesType.PHARMACY);
        types.add(GooglePlacesType.HOSPITAL);
        types.add(GooglePlacesType.BANK);

        recursivePlaceFind(0, types, results, radius, midpoint, callback);
    }

    private void recursivePlaceFind(final int index, final List<GooglePlacesType> types, final List<GooglePlaceResult> results, final int radius, final LatLng midpoint, final GooglePlacesResponseCallback callback) {
        logger.i("Gathering all neutral camps of type %s", types.get(index));
        GooglePlacesRequestPayload googlePlacesRequestPayload = new GooglePlacesRequestPayload();
        googlePlacesRequestPayload.setRadius(radius);
        googlePlacesRequestPayload.setType(types.get(index));
        googlePlacesRequestPayload.setLocation(midpoint);

        GooglePlacesApi googlePlacesApi = retrofit.get().create(GooglePlacesApi.class);
        final Call<GooglePlacesResponsePayload> googlePlacesApiCall = googlePlacesApi.retrieveNearbyPlaces(googlePlacesRequestPayload);
        googlePlacesApiCall.enqueue(new Callback<GooglePlacesResponsePayload>() {
            @Override
            public void onResponse(Call<GooglePlacesResponsePayload> call, Response<GooglePlacesResponsePayload> response) {
                int i = index + 1;
                results.addAll(response.body().getResults());
                if (i < types.size()) {
                    recursivePlaceFind(i, types, results, radius, midpoint, callback);
                } else if (response.code() == 200) {
                    logger.i("All neutral camps have been gathered");
                    callback.onResponse(results, response.code());
                } else {
                    logger.e("Call for %s received a status code response of %d", types.get(index), response.code());
                    callback.onResponse(null, response.code());
                }
            }
            @Override
            public void onFailure(Call<GooglePlacesResponsePayload> call, Throwable t) {
                logger.e("Failure in receiving neutral camps. Call to %s failed", call.request().url().toString(), t);
                callback.onFailure(t);
            }
        });
    }
}
