package com.ede.standyourground.networking.api.exchange.api;

import com.ede.standyourground.networking.api.exchange.payload.request.RoutesRequest;
import com.ede.standyourground.networking.api.model.Routes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface GoogleDirectionsApi {

    @POST("/routes")
    Call<Routes> calculateRoutes(@Body RoutesRequest routesRequest);
}
