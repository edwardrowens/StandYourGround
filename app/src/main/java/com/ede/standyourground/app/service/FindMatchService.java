package com.ede.standyourground.app.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;

import com.ede.standyourground.app.activity.FindMatchActivity;
import com.ede.standyourground.app.api.MatchMakingApi;
import com.ede.standyourground.app.to.FindMatchRequestTO;
import com.ede.standyourground.app.to.FindMatchResponseTO;
import com.ede.standyourground.framework.Logger;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class FindMatchService extends IntentService implements Runnable {

    private static final String FIND_MATCH_RESPONSE = FindMatchService.class.getName() + ".findMatchResponse";
    private static final String FIND_MATCH_REQUEST = FindMatchService.class.getName() + ".findMatchRequest";

    private static Logger logger = new Logger(FindMatchService.class);

    private Handler handler;

    private FindMatchRequestTO findMatchRequestTO;
    private ResultReceiver resultReceiver;

    FindMatchService() {
        super("FindMatchService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            resultReceiver = bundle.getParcelable(FindMatchActivity.FIND_MATCH_RESULT_RECEIVER);
            findMatchRequestTO = bundle.getParcelable(FIND_MATCH_REQUEST);
            findMatch();
        }
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
            e.printStackTrace();
        }

        return response;
    }

    private void findMatch() {
        if (handler == null) {
            Looper.prepare();
            handler = new Handler();
            Looper.loop();
        }
        Response<FindMatchResponseTO> response = findMatch(findMatchRequestTO);
        if (response.code() == 200) {
            logger.i("Found match!");
            Bundle bundle = new Bundle();
            bundle.putParcelable(FindMatchService.FIND_MATCH_RESPONSE, response.body());
            resultReceiver.send(0, bundle);
        } else {
            logger.i("Could not find match. Searching...");
            handler.postDelayed(this, 10000);
        }
    }
}
