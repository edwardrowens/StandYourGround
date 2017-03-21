package com.ede.standyourground.networking.api.exchange.api;

import com.ede.standyourground.networking.api.model.Routes;

import retrofit2.Call;
import retrofit2.http.GET;


public interface IpApi {

    @GET("/ip")
    Call<Routes> calculateRoutes();
}
