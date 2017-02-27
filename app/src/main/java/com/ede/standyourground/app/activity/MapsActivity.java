package com.ede.standyourground.app.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;

import com.ede.standyourground.R;
import com.ede.standyourground.app.model.Route;
import com.ede.standyourground.app.model.Routes;
import com.ede.standyourground.app.service.GoogleDirectionsService;
import com.ede.standyourground.app.service.StopGameService;
import com.ede.standyourground.framework.Logger;
import com.ede.standyourground.framework.api.MathService;
import com.ede.standyourground.framework.dagger.application.MyApp;
import com.ede.standyourground.framework.dagger.providers.GameSessionIdProvider;
import com.ede.standyourground.framework.dagger.providers.GoogleMapProvider;
import com.ede.standyourground.game.framework.management.impl.WorldManager;
import com.ede.standyourground.game.model.Units;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private final Logger logger = new Logger(MapsActivity.class);
    private static final int CAMERA_PADDING = 200;

    // VIEWS
    private HorizontalScrollView unitChoicesScrollView;
    private Button confirmRouteButton;

    private GoogleMap googleMap;
    private List<Marker> waypoints = new ArrayList<>();
    private boolean bound = false;
    private GoogleDirectionsService googleDirectionsService;
    private Units selectedUnit;
    private LatLng playerLocation;
    private LatLng opponentLocation;

    @Inject
    WorldManager worldManager;
    @Inject
    MathService mathService;
    @Inject
    GoogleMapProvider googleMapProvider;
    @Inject
    GameSessionIdProvider gameSessionIdProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        playerLocation = (LatLng) getIntent().getExtras().get(FindMatchActivity.PLAYER_LOCATION);
        opponentLocation = (LatLng) getIntent().getExtras().get(FindMatchActivity.OPPONENT_LOCATION);
        gameSessionIdProvider.setGameSessionId(UUID.fromString((String) getIntent().getExtras().get(FindMatchActivity.GAME_SESSION_ID)));

        logger.i("Player location is " + playerLocation.toString());
        logger.i("Opponent location is " + opponentLocation.toString());
    }


    @Override
    protected void onStart() {
        super.onStart();

        // Bind to GoogleDirectionsService
        Intent intent = new Intent(this, GoogleDirectionsService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (bound) {
            unbindService(serviceConnection);
            bound = false;
        }
    }

    @Override
    protected void onDestroy() {
        logger.d("Ending game");
        Intent intent = new Intent(this, StopGameService.class);
        intent.putExtra(FindMatchActivity.GAME_SESSION_ID, gameSessionIdProvider.getGameSessionId());
        startService(intent);
        super.onDestroy();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        logger.i("Starting map");
        this.googleMap = googleMap;
        googleMapProvider.setGoogleMap(googleMap);

        worldManager.start();

        // Updating google map settings
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setCompassEnabled(false);

        // Add markers representing the opponent's and the player's location
        this.googleMap.addMarker(new MarkerOptions().position(opponentLocation));
        this.googleMap.addMarker(new MarkerOptions().position(playerLocation));

        // Set up the camera's initial position
        LatLngBounds latLngBounds = LatLngBounds.builder().include(opponentLocation).include(playerLocation).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(latLngBounds, CAMERA_PADDING);
        googleMap.animateCamera(cameraUpdate, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                float zoom = googleMap.getCameraPosition().zoom;
                CameraPosition cameraPosition = CameraPosition.builder()
                        .target(mathService.midpoint(playerLocation, opponentLocation))
                        .bearing(mathService.bearing(playerLocation, opponentLocation))
                        .zoom(zoom)
                        .build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), new GoogleMap.CancelableCallback() {
                    @Override
                    public void onFinish() {
                        googleMap.getUiSettings().setRotateGesturesEnabled(false);
                    }

                    @Override
                    public void onCancel() {
                    }
                });
            }

            @Override
            public void onCancel() {
            }
        });

        confirmRouteButton = (Button) findViewById(R.id.confirmRouteButton);
        unitChoicesScrollView = (HorizontalScrollView) findViewById(R.id.unitChoicesScrollView);
    }

    public void onRoute(View view) {
        logger.i("On route clicked");
        drawRoutesForUnit();

        // TODO DELETE
        logger.i("Creating enemy foot soldier.");
        googleDirectionsService.getRoutes(opponentLocation, playerLocation, new ArrayList<LatLng>(),
                new Callback<Routes>() {
                    @Override
                    public void onResponse(Call<Routes> call, Response<Routes> response) {
                        logger.i("Successfully created enemy foot soldier.");
                        Polyline polyline = drawRoute(response.body().getRoutes().get(0));
                        worldManager.createEnemyUnit(polyline.getPoints(), opponentLocation, selectedUnit);
                    }

                    @Override
                    public void onFailure(Call<Routes> call, Throwable t) {
                        logger.e("Failure in routing enemy foot soldier", t);
                    }
                });
        // TODO END OF DELETE

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
            }
        });

        for (Marker marker : waypoints)
            marker.remove();
        waypoints.clear();
        confirmRouteButton.setVisibility(View.GONE);
        unitChoicesScrollView.setVisibility(View.VISIBLE);
    }

    public void onSelectUnit(View view) {
        unitChoicesScrollView.setVisibility(View.GONE);
        confirmRouteButton.setVisibility(View.VISIBLE);
        switch (view.getId()) {
            case R.id.footSoldier:
                selectedUnit = Units.FOOT_SOLDIER;
                break;
        }

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                waypoints.add(googleMap.addMarker(markerOptions));
            }
        });
    }

    private void drawRoutesForUnit() {
        ArrayList<LatLng> waypointsLatLng = new ArrayList<>();
        for (Marker marker : waypoints)
            waypointsLatLng.add(marker.getPosition());

        googleDirectionsService.getRoutes(playerLocation, opponentLocation, waypointsLatLng,
                new Callback<Routes>() {
                    @Override
                    public void onResponse(Call<Routes> call, Response<Routes> response) {
                        logger.i("response with routes received");
                        Polyline polyline = drawRoute(response.body().getRoutes().get(0));

                        worldManager.createPlayerUnit(polyline.getPoints(), playerLocation, selectedUnit);
                    }

                    @Override
                    public void onFailure(Call<Routes> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            bound = true;
            GoogleDirectionsService.LocalBinder binder = (GoogleDirectionsService.LocalBinder) iBinder;
            googleDirectionsService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bound = false;
        }
    };

    private Polyline drawRoute(Route route) {
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions = polylineOptions.addAll(PolyUtil.decode(route.getOverviewPolyline().getPoints()))
                .width(20)
                .color(Color.BLACK);
        logger.i("drawing route");
        return googleMap.addPolyline(polylineOptions);
    }
}
