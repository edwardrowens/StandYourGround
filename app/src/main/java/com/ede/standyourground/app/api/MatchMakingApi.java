package com.ede.standyourground.app.api;

import com.ede.standyourground.app.to.FindMatchRequestTO;
import com.ede.standyourground.app.to.FindMatchResponseTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Eddie on 2/9/2017.
 */

public interface MatchMakingApi {

    @POST("/routes")
    Call<FindMatchResponseTO> findMatch(@Body FindMatchRequestTO findMatchRequestTO);
}
