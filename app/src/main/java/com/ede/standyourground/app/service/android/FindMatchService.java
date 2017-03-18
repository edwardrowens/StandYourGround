package com.ede.standyourground.app.service.android;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;

import com.ede.standyourground.app.activity.FindMatchActivity;
import com.ede.standyourground.app.api.MatchMakingApi;
import com.ede.standyourground.framework.Logger;
import com.ede.standyourground.networking.exchange.request.FindMatchRequest;
import com.ede.standyourground.networking.exchange.response.FindMatchResponse;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class FindMatchService extends Service implements Runnable {

    public static final String FIND_MATCH_RESPONSE = FindMatchService.class.getName() + ".findMatchResponse";
    public static final String FIND_MATCH_REQUEST = FindMatchService.class.getName() + ".findMatchRequest";

    private Handler handler;
    private static HandlerThread handlerThread;

    private static Logger logger = new Logger(FindMatchService.class);

    private FindMatchRequest findMatchRequest;
    private ResultReceiver resultReceiver;
    private static AtomicBoolean runThread = new AtomicBoolean(true);


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handlerThread = new HandlerThread("FindMatch");
        Bundle bundle = null;
        if (intent != null) {
            bundle = intent.getExtras();
        }
        if (bundle != null) {
            resultReceiver = bundle.getParcelable(FindMatchActivity.FIND_MATCH_RESULT_RECEIVER);
            findMatchRequest = bundle.getParcelable(FIND_MATCH_REQUEST);
        }
        runThread.set(true);
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        handler.post(this);
        return START_NOT_STICKY;
    }

    @Override
    public void run() {
        findMatch();
    }

    public Response<FindMatchResponse> findMatch(FindMatchRequest findMatchRequest) {
        MatchMakingApi matchMakingApi = ServiceGenerator.createService(MatchMakingApi.class);

        Call<FindMatchResponse> findMatchCall = matchMakingApi.findMatch(findMatchRequest);

        Response<FindMatchResponse> response = null;
        try {
            response = findMatchCall.execute();
        } catch (IOException e) {
            logger.e("Problem handling the request", e);
            response = Response.error(503, ResponseBody.create(null, "error"));
        }

        return response;
    }

    private void findMatch() {
        Response<FindMatchResponse> response = findMatch(findMatchRequest);
        if (response.code() == 200) {
            logger.i("Found match!");
            Bundle bundle = new Bundle();
            bundle.putParcelable(FindMatchService.FIND_MATCH_RESPONSE, response.body());
            resultReceiver.send(response.code(), bundle);
        } else if (response.code() == 503) {
            logger.i("Server is down");
            resultReceiver.send(response.code(), null);
        } else if (response.code() == 204) {
            if (runThread.get()) {
                logger.i("Could not find match. Searching...");
                handler.postDelayed(this, 1000);
            }
        } else {
            logger.e("Problem in matchmaking request. Code %d", response.code());
            resultReceiver.send(response.code(), null);
        }
    }

    public static void stopThread() {
        if (handlerThread != null && handlerThread.isAlive()) {
            handlerThread.quit();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        logger.i("Stopping find game thread");
        runThread.set(false);
        if (handler != null) {
            handler.removeCallbacks(this);
        }
    }
}
