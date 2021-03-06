package com.ede.standyourground.app.activity.service;

import android.app.IntentService;
import android.content.Intent;

import com.ede.standyourground.app.activity.FindMatchActivity;
import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.framework.api.dagger.application.MyApp;
import com.ede.standyourground.framework.api.transmit.EmptyCallback;
import com.ede.standyourground.networking.api.exchange.api.GamesApi;

import java.util.UUID;

import retrofit2.Call;

public class StopGameService extends IntentService {

    private static Logger logger = new Logger(StopGameService.class);

    public StopGameService() {
        super("StopGameService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            logger.i("Making call to remove game.");
            UUID gameSessionId = (UUID) intent.getExtras().get(FindMatchActivity.GAME_SESSION_ID);
            GamesApi gamesApi = MyApp.getAppComponent().getRetrofit().get().create(GamesApi.class);
            Call<Void> endGameCall = gamesApi.endGame(gameSessionId);
            endGameCall.enqueue(new EmptyCallback<Void>());
        }
    }

}
