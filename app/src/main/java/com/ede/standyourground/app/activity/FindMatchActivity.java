package com.ede.standyourground.app.activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ede.standyourground.R;
import com.ede.standyourground.app.service.FindMatchService;
import com.ede.standyourground.app.to.FindMatchRequestTO;
import com.ede.standyourground.app.to.FindMatchResponseTO;
import com.ede.standyourground.framework.Logger;
import com.ede.standyourground.framework.Receiver;
import com.ede.standyourground.framework.StandYourGroundResultReceiver;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;

public class FindMatchActivity extends AppCompatActivity implements Receiver, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static String FIND_MATCH_RESULT_RECEIVER = FindMatchActivity.class.getName() + ".findMatchResultReceiver";
    public static String OPPONENT_LOCATION = FindMatchActivity.class.getName() + ".opponent";
    public static String PLAYER_LOCATION = FindMatchActivity.class.getName() + ".location";

    private static final long LOCATION_INTERVAL = 5000;

    private static Logger logger = new Logger(FindMatchActivity.class);

    private StandYourGroundResultReceiver standYourGroundResultReceiver = new StandYourGroundResultReceiver(new Handler());
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location currentLocation;
    private UUID playerId;

    private Button onSearchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_match);

        onSearchButton = (Button) findViewById(R.id.onSearchButton);

        standYourGroundResultReceiver.setReceiver(this);
        getLocation();
    }

    public void onSearch(View view) {
        logger.i("Search button clicked!");
        Intent intent = new Intent(this, FindMatchService.class);
        intent.putExtra(FindMatchActivity.FIND_MATCH_RESULT_RECEIVER, standYourGroundResultReceiver);

        if (playerId == null)
            playerId = UUID.randomUUID();

        FindMatchRequestTO findMatchRequestTO = new FindMatchRequestTO();
        findMatchRequestTO.setId(playerId);
        findMatchRequestTO.setLat(currentLocation.getLatitude());
        findMatchRequestTO.setLng(currentLocation.getLongitude());
        findMatchRequestTO.setRadius(5);

        intent.putExtra(FindMatchService.FIND_MATCH_REQUEST, findMatchRequestTO);

        this.startService(intent);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        logger.i("Match found!");

        // Don't need to see location anymore.
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        Intent intent = new Intent(this, MapsActivity.class);

        FindMatchResponseTO findMatchResponseTO = (FindMatchResponseTO) resultData.get(FindMatchService.FIND_MATCH_RESPONSE);
        LatLng opponentLocation = new LatLng(findMatchResponseTO.getLat(), findMatchResponseTO.getLng());

        intent.putExtra(OPPONENT_LOCATION, opponentLocation);
        intent.putExtra(PLAYER_LOCATION, new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
        this.startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }


    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(MapsActivity.class.getName(), "Google API client connected");
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        } catch (SecurityException e) {
            Log.i(MapsActivity.class.getName(), "Fine location permission not granted");
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        onSearchButton.setEnabled(true);
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

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
