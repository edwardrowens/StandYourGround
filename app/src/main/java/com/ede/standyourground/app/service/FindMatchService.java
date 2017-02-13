package com.ede.standyourground.app.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;

import com.ede.standyourground.app.activity.FindMatchActivity;
import com.ede.standyourground.app.api.MatchMakingApi;
import com.ede.standyourground.app.to.FindMatchRequestTO;
import com.ede.standyourground.app.to.FindMatchResponseTO;
import com.ede.standyourground.framework.Logger;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.Response;

public class FindMatchService extends Service implements Runnable {

    public static final String FIND_MATCH_RESPONSE = FindMatchService.class.getName() + ".findMatchResponse";
    public static final String FIND_MATCH_REQUEST = FindMatchService.class.getName() + ".findMatchRequest";

    private Handler handler;

    private static Logger logger = new Logger(FindMatchService.class);

    private FindMatchRequestTO findMatchRequestTO;
    private ResultReceiver resultReceiver;
    private static AtomicBoolean runThread = new AtomicBoolean(true);


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = null;
        if (intent != null) {
            bundle = intent.getExtras();
        }
        if (bundle != null) {
            resultReceiver = bundle.getParcelable(FindMatchActivity.FIND_MATCH_RESULT_RECEIVER);
            findMatchRequestTO = bundle.getParcelable(FIND_MATCH_REQUEST);
        }
        runThread.set(true);
        new Thread(this).start();
        return START_NOT_STICKY;
    }

    @Override
    public void run() {
        findMatch();
    }

    public Response<FindMatchResponseTO> findMatch(FindMatchRequestTO findMatchRequestTO) {
        MatchMakingApi matchMakingApi = ServiceGenerator.createService(MatchMakingApi.class);

        Call<FindMatchResponseTO> findMatchCall = matchMakingApi.findMatch(findMatchRequestTO);

        Response<FindMatchResponseTO> response = null;
        try {
            response = findMatchCall.execute();
        } catch (IOException e) {
            logger.e("Problem handling the request", e);
        }

        return response;
    }

    private void findMatch() {
        if (handler == null) {
            Looper.prepare();
            handler = new Handler();
            handler.post(this);
            Looper.loop();
        }
        if (runThread.get()) {
            Response<FindMatchResponseTO> response = findMatch(findMatchRequestTO);
            if (response.code() == 200) {
                logger.i("Found match!");
                Bundle bundle = new Bundle();
                bundle.putParcelable(FindMatchService.FIND_MATCH_RESPONSE, response.body());
                resultReceiver.send(FindMatchRequestTO.REQUEST_CODE, bundle);
            } else {
                if (runThread.get()) {
                    logger.i("Could not find match. Searching...");
                    handler.postDelayed(this, 10000);
                }
            }
        }
    }

    public static void stopThread() {
        runThread.set(false);
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
