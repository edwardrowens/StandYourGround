package com.ede.standyourground.networking.api.exchange.api;

import com.ede.standyourground.networking.api.exchange.payload.response.GooglePlaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 *
 */

public interface GooglePlacesApi {

    @GET("https://maps.googleapis.com/maps/api/place/nearbysearch/json?&key=AIzaSyCfQneMNWa5A_a51MrsNC266-Lqb3A4BvQ")
    Call<GooglePlaces> retrieveNearbyPlaces(@Query("type") String type, @Query("location") String location, @Query("radius") int radius);
}
