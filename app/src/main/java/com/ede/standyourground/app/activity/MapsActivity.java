package com.ede.standyourground.app.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;

import com.ede.standyourground.R;
import com.ede.standyourground.app.model.Route;
import com.ede.standyourground.app.model.Routes;
import com.ede.standyourground.app.service.GoogleDirectionsService;
import com.ede.standyourground.app.service.MathUtils;
import com.ede.standyourground.framework.Logger;
import com.ede.standyourground.framework.WorldManager;
import com.ede.standyourground.game.model.FootSoldier;
import com.ede.standyourground.game.model.MovableUnit;
import com.ede.standyourground.game.model.Units;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final long LOCATION_INTERVAL = 5000;
    private final Logger logger = new Logger(MapsActivity.class);
    private static final int CAMERA_PADDING = 200;

    // VIEWS
    private HorizontalScrollView unitChoicesScrollView;
    private Button confirmRouteButton;

    private GoogleMap googleMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Marker currentLocationMarker;
    private Marker targetLocationMarker;
    private List<Marker> waypoints = new ArrayList<>();
    private boolean bound = false;
    private GoogleDirectionsService googleDirectionsService;
    private Units selectedUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getLocation();
    }


    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();

        // Bind to GoogleDirectionsService
        Intent intent = new Intent(this, GoogleDirectionsService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }


    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
        if (bound) {
            unbindService(serviceConnection);
            bound = false;
        }
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
        logger.i("starting map");
        this.googleMap = googleMap;

        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setCompassEnabled(false);

        LatLng latLng = new LatLng(34.154319, -118.227570);
        targetLocationMarker = this.googleMap.addMarker(new MarkerOptions().position(latLng));

        confirmRouteButton = (Button) findViewById(R.id.confirmRouteButton);
        unitChoicesScrollView = (HorizontalScrollView) findViewById(R.id.unitChoicesScrollView);
    }


    @Override
    public void onLocationChanged(Location location) {
        logger.i("Location changed to %s", location.toString());
        WorldManager.getInstance().startLoop();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        currentLocationMarker = this.googleMap.addMarker(new MarkerOptions().position(latLng));
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);

        LatLngBounds latLngBounds = LatLngBounds.builder().include(targetLocationMarker.getPosition()).include(currentLocationMarker.getPosition()).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(latLngBounds, CAMERA_PADDING);
        googleMap.animateCamera(cameraUpdate, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                float zoom = googleMap.getCameraPosition().zoom;
                CameraPosition cameraPosition = CameraPosition.builder()
                        .target(MathUtils.midpoint(currentLocationMarker.getPosition(), targetLocationMarker.getPosition()))
                        .bearing(MathUtils.bearing(currentLocationMarker.getPosition(), targetLocationMarker.getPosition()))
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


    }

    public void onRoute(View view) {
        logger.i("On route clicked");
        drawRoutesForUnit();

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                return;
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
        switch(view.getId()) {
            case R.id.footSoldier:
                selectedUnit = Units.FOOT_SOLDIER;
                logger.i("Selected foot soldier");
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

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void drawRoutesForUnit() {
        ArrayList<LatLng> waypointsLatLng = new ArrayList<>();
        for (Marker marker : waypoints)
            waypointsLatLng.add(marker.getPosition());

        googleDirectionsService.getRoutes(currentLocationMarker.getPosition(), targetLocationMarker.getPosition(), waypointsLatLng,
                new Callback<Routes>() {
                    @Override
                    public void onResponse(Call<Routes> call, Response<Routes> response) {
                        logger.i("response with routes received");
                        Polyline polyline = drawRoute(response.body().getRoutes().get(0));
                        Circle circle = googleMap.addCircle(new CircleOptions().center(currentLocationMarker.getPosition()).clickable(false).radius(50).fillColor(Color.BLUE).strokeColor(Color.BLUE).zIndex(1.0f));
                        MovableUnit unit = null;
                        switch (selectedUnit) {
                            case FOOT_SOLDIER:
                                unit = new FootSoldier(100, polyline, currentLocationMarker.getPosition(), circle);
                                break;
                        }
                        if (unit != null) {
                            WorldManager.getInstance().addUnit(unit);
                        }
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


    private Polyline drawRoute(Route route) {
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions = polylineOptions.addAll(PolyUtil.decode(route.getOverviewPolyline().getPoints()))
                .width(20)
                .color(Color.BLACK);
        logger.i("drawing route");
        return googleMap.addPolyline(polylineOptions);
    }
}
