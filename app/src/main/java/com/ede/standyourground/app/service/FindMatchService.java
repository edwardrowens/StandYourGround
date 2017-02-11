package com.ede.standyourground.app.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.ede.standyourground.app.api.MatchMakingApi;
import com.ede.standyourground.app.to.FindMatchRequestTO;
import com.ede.standyourground.app.to.FindMatchResponseTO;
import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindMatchService extends Service {

    private final IBinder iBinder = new FindMatchService.LocalBinder();

    public class LocalBinder extends Binder {
        public FindMatchService getService() {
            return FindMatchService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    public void findMatch(LatLng position, int radius, UUID id, final Callback<FindMatchResponseTO> callback) {
        MatchMakingApi matchMakingApi = ServiceGenerator.createService(MatchMakingApi.class);

        FindMatchRequestTO findMatchRequestTO = new FindMatchRequestTO();
        findMatchRequestTO.setLat(position.latitude);
        findMatchRequestTO.setLng(position.longitude);
        findMatchRequestTO.setRadius(radius);
        findMatchRequestTO.setId(id);

        Call<FindMatchResponseTO> findMatchCall = matchMakingApi.findMatch(findMatchRequestTO);

        findMatchCall.enqueue(new Callback<FindMatchResponseTO>() {
            @Override
            public void onResponse(Call<FindMatchResponseTO> call, Response<FindMatchResponseTO> response) {
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<FindMatchResponseTO> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }
}
