package com.ede.standyourground.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ede.standyourground.api.DirectionsApi;
import com.ede.standyourground.model.Routes;
import com.ede.standyourground.to.RoutesRequestTO;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoogleDirectionsService extends Service {

    private static final String TAG = GoogleDirectionsService.class.getName();

    private final IBinder iBinder = new LocalBinder();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }


    public class LocalBinder extends Binder {
        public GoogleDirectionsService getService() {
            return GoogleDirectionsService.this;
        }
    }


    public void getRoutes(LatLng origin, LatLng destination, List<LatLng> waypoints, final Callback<Routes> callback) {
        DirectionsApi directionsApi = ServiceGenerator.createService(DirectionsApi.class);

        RoutesRequestTO routesRequestTO = new RoutesRequestTO();
        routesRequestTO.setEndLat(destination.latitude);
        routesRequestTO.setEndLng(destination.longitude);
        routesRequestTO.setStartLat(origin.latitude);
        routesRequestTO.setStartLng(origin.longitude);
        routesRequestTO.setWaypoints(waypoints);

        Call<Routes> routesCall = directionsApi.calculateRoutes(routesRequestTO);

        Log.i(TAG, "beginning call for directions");
        routesCall.enqueue(new Callback<Routes>() {
            @Override
            public void onResponse(Call<Routes> call, Response<Routes> response) {
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<Routes> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }
}
