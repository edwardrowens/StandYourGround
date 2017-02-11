package com.ede.standyourground.app.api;

import com.ede.standyourground.app.model.Routes;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Eddie on 2/11/2017.
 */

public interface IpApi {

    @GET("/ip")
    Call<Routes> calculateRoutes();
}
