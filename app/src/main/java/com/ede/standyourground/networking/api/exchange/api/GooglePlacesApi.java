package com.ede.standyourground.networking.api.exchange.api;

import com.ede.standyourground.networking.api.exchange.payload.request.GooglePlacesRequestPayload;
import com.ede.standyourground.networking.api.exchange.payload.response.GooglePlacesResponsePayload;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 *
 */

public interface GooglePlacesApi {

    @POST("/places/nearbysearch")
    Call<GooglePlacesResponsePayload> retrieveNearbyPlaces(@Body GooglePlacesRequestPayload googlePlacesRequestPayload);
}
