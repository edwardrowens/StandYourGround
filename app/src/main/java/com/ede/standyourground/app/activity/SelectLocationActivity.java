package com.ede.standyourground.app.activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ede.standyourground.R;
import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.framework.api.dagger.application.MyApp;
import com.ede.standyourground.framework.api.service.DirectionsService;
import com.ede.standyourground.framework.api.service.LatLngService;
import com.ede.standyourground.game.api.model.GameMode;
import com.ede.standyourground.networking.api.model.Routes;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Random;
import java.util.UUID;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectLocationActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final Logger logger = new Logger(SelectLocationActivity.class);

    public static String GAME_MODE = SelectLocationActivity.class.getName() + ".gameMode";

    private static final long LOCATION_INTERVAL = 5000;

    private GoogleMap googleMap;
    private GoogleApiClient googleApiClient;
    private LatLng playerLocation;
    private Marker marker;
    private Circle enemySearchRadius;
    private GameMode gameMode;
    private int enemyRangeInKm = 2;
    private ViewGroup settingUpGamePromptContainer;
    private Button confirmButton;

    @Inject
    LatLngService latLngService;
    @Inject
    DirectionsService directionsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApp.getAppComponent().inject(this);

        gameMode = (GameMode) getIntent().getExtras().get(GAME_MODE);

        setContentView(R.layout.activity_select_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.selectLocationMap);
        mapFragment.getMapAsync(this);

        settingUpGamePromptContainer = (ViewGroup) findViewById(R.id.settingUpGamePromptContainer);
        confirmButton = (Button) findViewById(R.id.confirmLocationButton);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        final Button confirmButton = (Button) findViewById(R.id.confirmLocationButton);
        final Button cancelButton = (Button) findViewById(R.id.cancelLocationButton);
        final SeekBar seekBar = (SeekBar) findViewById(R.id.specifyEnemyRange);

        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setCompassEnabled(false);
        googleMap.getUiSettings().setAllGesturesEnabled(false);

        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                enemySearchRadius.setCenter(marker.getPosition());
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                enemySearchRadius.setCenter(marker.getPosition());
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                enemySearchRadius.setCenter(marker.getPosition());
            }
        });

        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(playerLocation, 12), new GoogleMap.CancelableCallback() {
                    @Override
                    public void onFinish() {
                        googleMap.getUiSettings().setAllGesturesEnabled(true);
                        marker = googleMap.addMarker(new MarkerOptions().draggable(true).position(playerLocation).title("Drag me to set up your base!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                        marker.showInfoWindow();
                        seekBar.setClickable(true);
                        confirmButton.setEnabled(true);
                        cancelButton.setEnabled(true);
                        enemySearchRadius = googleMap.addCircle(new CircleOptions().center(playerLocation).fillColor(getResources().getColor(R.color.friendlyKingdomBlue)).radius(2000));
                        final TextView seekBarOutput = (TextView) findViewById(R.id.seekBarOutput);

                        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                enemyRangeInKm = progress + 2;
                                enemySearchRadius.setRadius(enemyRangeInKm * 1000);
                                seekBarOutput.setText(getString(R.string.enemyRange, enemyRangeInKm));
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }
                        });
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        logger.i("Google API client connected");
        try {
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(LOCATION_INTERVAL);
            locationRequest.setFastestInterval(LOCATION_INTERVAL);
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        } catch (SecurityException e) {
            logger.e("Fine location permission not granted", e);
        }
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
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        playerLocation = new LatLng(location.getLatitude(), location.getLongitude());
    }

    public void onConfirmLocation(final View view) {
        settingUpGamePromptContainer.setVisibility(View.VISIBLE);
        confirmButton.setEnabled(false);

        Random random = new Random();
        int opponentDistanceInKm = random.nextInt(enemyRangeInKm + 1 - 2) + 2;
        final LatLng opponentLocation = latLngService.generateRandomLocation(marker.getPosition(), opponentDistanceInKm * 1000);
        directionsService.getRoutes(playerLocation, opponentLocation, null, new Callback<Routes>() {
            @Override
            public void onResponse(Call<Routes> call, Response<Routes> response) {
                if (response.body().getRoutes().isEmpty()) {
                    Toast.makeText(SelectLocationActivity.this, "Choose a new location. The one you have chosen is not reachable!", Toast.LENGTH_LONG).show();
                    onCancelLocation(view);
                    centerCamera();
                } else {
                    switch (gameMode) {
                        case SINGLE_PLAYER:
                            singlePlayerGame(opponentLocation);
                            break;
                        case MULTIPLAYER:
                            multiplayerGame();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Routes> call, Throwable t) {

            }
        });
    }

    public void onCancelLocation(View view) {
        settingUpGamePromptContainer.setVisibility(View.GONE);
        confirmButton.setEnabled(true);
        marker.setPosition(playerLocation);
        enemySearchRadius.setCenter(playerLocation);
    }

    private void multiplayerGame() {
        Intent intent = new Intent(this, FindMatchActivity.class);
        intent.putExtra(FindMatchActivity.PLAYER_LOCATION, marker.getPosition());
        intent.putExtra(FindMatchActivity.ENEMY_SEARCH_RADIUS, enemyRangeInKm);
        startActivity(intent);
    }

    private void singlePlayerGame(LatLng opponentLocation) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra(MapsActivity.OPPONENT_LOCATION, opponentLocation);
        intent.putExtra(MapsActivity.PLAYER_LOCATION, marker.getPosition());
        intent.putExtra(MapsActivity.GAME_SESSION_ID, UUID.randomUUID().toString());
        intent.putExtra(MapsActivity.GAME_MODE, GameMode.SINGLE_PLAYER);
        startActivity(intent);
    }

    private void centerCamera() {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(playerLocation, 12));
    }
}
