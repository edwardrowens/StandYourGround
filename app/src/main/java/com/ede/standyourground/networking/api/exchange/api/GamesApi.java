package com.ede.standyourground.networking.api.exchange.api;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Path;

/**
 * Created by Eddie on 2/19/2017.
 */

public interface GamesApi {

    @DELETE("/games/{gameSessionId}")
    Call<Void> endGame(@Path(value = "gameSessionId") UUID gameSessionId);
}
