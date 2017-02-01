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
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoogleDirectionsService extends Service {

    public static final String ORIGIN = "com.ede.standyourground.GoogleDirectionsService.extras.origin";
    public static final String DESTINATION = "com.ede.standyourground.GoogleDirectionsService.extras.destination";
    public static final String WAYPOINTS = "com.ede.standyourground.GoogleDirectionsService.extras.waypoints";

    private static final String TAG = GoogleDirectionsService.class.getName();
    private static final String BASE_URL = "http://10.0.2.2:8000/";

    private final IBinder iBinder = new LocalBinder();
    private Routes routes;


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


    public Routes getRoutes(LatLng origin, LatLng destination, List<LatLng> waypoints) {
        OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.client(httpClient.build()).build();

        DirectionsApi directionsApi = retrofit.create(DirectionsApi.class);
        RoutesRequestTO routesRequestTO = new RoutesRequestTO();
        routesRequestTO.setEndLat(destination.latitude);
        routesRequestTO.setEndLng(destination.longitude);
        routesRequestTO.setStartLat(origin.latitude);
        routesRequestTO.setStartLng(origin.longitude);
        routesRequestTO.setWaypoints(waypoints);
        Call<Routes> call = directionsApi.calculateRoutes(routesRequestTO);

        final AtomicReference<Routes> routes = new AtomicReference<>();
        call.enqueue(new Callback<Routes>() {
            @Override
            public void onResponse(Call<Routes> call, Response<Routes> response) {
                Log.i(TAG, "successful call made");
                routes.set(response.body());
            }

            @Override
            public void onFailure(Call<Routes> call, Throwable t) {
                Log.i(TAG, "unsuccessful call made");
            }
        });

        return routes.get();
    }
}
