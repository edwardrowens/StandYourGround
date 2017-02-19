package com.ede.standyourground.app.api;

import com.ede.standyourground.app.model.Routes;
import com.ede.standyourground.app.to.RoutesRequestTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface DirectionsApi {

    @POST("/routes")
    Call<Routes> calculateRoutes(@Body RoutesRequestTO routesRequestTO);
}
