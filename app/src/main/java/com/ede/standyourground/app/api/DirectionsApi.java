package com.ede.standyourground.app.api;

import com.ede.standyourground.app.model.Routes;
import com.ede.standyourground.app.to.RoutesRequestTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Eddie on 1/30/2017.
 */

public interface DirectionsApi {

    @POST("/routes")
    Call<Routes> calculateRoutes(@Body RoutesRequestTO routesRequestTO);
}
