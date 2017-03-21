package com.ede.standyourground.app.activity.service;

import android.app.IntentService;
import android.content.Intent;

import com.ede.standyourground.app.activity.FindMatchActivity;
import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.framework.api.dagger.application.MyApp;
import com.ede.standyourground.framework.api.transmit.EmptyCallback;
import com.ede.standyourground.networking.api.exchange.api.MatchMakingApi;

import java.util.UUID;

import retrofit2.Call;

public class RemoveFromMatchMakingService extends IntentService {

    private static Logger logger = new Logger(RemoveFromMatchMakingService.class);

    public RemoveFromMatchMakingService() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            logger.i("Removing player from match making");
            UUID playerId = (UUID) intent.getExtras().get(FindMatchActivity.PLAYER_ID);
            MatchMakingApi matchMakingApi = MyApp.getAppComponent().getRetrofit().get().create(MatchMakingApi.class);
            Call<Void> deleteMatchedPlayerCall = matchMakingApi.deleteMatchedPlayer(playerId);
            deleteMatchedPlayerCall.enqueue(new EmptyCallback<Void>());
        }
    }
}
