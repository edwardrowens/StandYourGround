package com.ede.standyourground.networking.api.exchange.api;

import com.ede.standyourground.networking.api.exchange.payload.request.FindMatchRequest;
import com.ede.standyourground.networking.api.exchange.payload.response.FindMatchResponse;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface MatchMakingApi {

    @POST("/matchmaking")
    Call<FindMatchResponse> findMatch(@Body FindMatchRequest findMatchRequest);

    @DELETE("/matchmaking/players/{playerId}")
    Call<Void> deleteMatchedPlayer(@Path(value="playerId") UUID playerId);
}
