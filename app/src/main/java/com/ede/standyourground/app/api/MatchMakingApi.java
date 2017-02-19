package com.ede.standyourground.app.api;

import com.ede.standyourground.app.to.FindMatchRequestTO;
import com.ede.standyourground.app.to.FindMatchResponseTO;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface MatchMakingApi {

    @POST("/matchmaking")
    Call<FindMatchResponseTO> findMatch(@Body FindMatchRequestTO findMatchRequestTO);

    @DELETE("/matchmaking/players/{playerId}")
    Call<Void> deleteMatchedPlayer(@Path(value="playerId") UUID playerId);
}
