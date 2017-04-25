package com.ede.standyourground.app.activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.ede.standyourground.R;
import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.framework.api.service.LatLngService;
import com.ede.standyourground.game.api.model.GameMode;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;

import javax.inject.Inject;

public class SettingUpMatchActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final Logger logger = new Logger(SettingUpMatchActivity.class);

    private static final long LOCATION_INTERVAL = 5000;
    private static final int DISTANCE_FOR_OPPONENT = 3000;

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;

    @Inject
    LatLngService latLngService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_up_match);

        getLocation();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        logger.i("Google API client connected");
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        } catch (SecurityException e) {
            logger.e("Fine location permission not granted", e);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        TextView textView = (TextView) findViewById(R.id.creatingGameText);
        textView.setText(getString(R.string.game_ready));
        final LatLng playerLocation = new LatLng(location.getLatitude(), location.getLongitude());
        final LatLng opponentLocation = latLngService.generateRandomLocation(playerLocation, DISTANCE_FOR_OPPONENT);
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra(MapsActivity.OPPONENT_LOCATION, opponentLocation);
        intent.putExtra(MapsActivity.PLAYER_LOCATION, playerLocation);
        intent.putExtra(MapsActivity.GAME_SESSION_ID, UUID.randomUUID());
        intent.putExtra(MapsActivity.GAME_MODE, GameMode.SINGLE_PLAYER);
        startActivity(intent);
    }

    private void getLocation() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(LOCATION_INTERVAL);
        locationRequest.setFastestInterval(LOCATION_INTERVAL);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }
}
