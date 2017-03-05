package com.ede.standyourground.app.service.android;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.ede.standyourground.app.api.DirectionsApi;
import com.ede.standyourground.app.model.Routes;
import com.ede.standyourground.app.to.RoutesRequestTO;
import com.ede.standyourground.framework.api.RouteService;
import com.ede.standyourground.framework.dagger.application.MyApp;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoogleDirectionsService extends Service {

    private final IBinder iBinder = new LocalBinder();

    @Inject RouteService routeService;

    @Override
    public void onCreate() {
        ((MyApp) getApplication()).getAppComponent().inject(this);
    }

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

        routesCall.enqueue(new Callback<Routes>() {
            @Override
            public void onResponse(Call<Routes> call, Response<Routes> response) {
                response.body().setTotalDistance(routeService.getTotalDistance(response.body()));
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<Routes> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }
}
