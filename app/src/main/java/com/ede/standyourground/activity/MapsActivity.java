package com.ede.standyourground.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.ede.standyourground.R;
import com.ede.standyourground.model.Route;
import com.ede.standyourground.model.Routes;
import com.ede.standyourground.service.GoogleDirectionsService;
import com.ede.standyourground.service.MathUtils;
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
    private static final String TAG = MapsActivity.class.getName();
    private static final int CAMERA_PADDING = 100;

    private GoogleMap googleMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Marker currentLocationMarker;
    private Marker targetLocationMarker;
    private List<Marker> waypoints = new ArrayList<>();
    private boolean bound = false;
    private GoogleDirectionsService googleDirectionsService;
    private Polyline polylineRoute;


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
        this.googleMap = googleMap;

        LatLng latLng = new LatLng(34.155323, -118.247092);
        targetLocationMarker = this.googleMap.addMarker(new MarkerOptions().position(latLng));

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
    public void onLocationChanged(Location location) {
        Log.i(TAG, String.format("Location changed to %s", location.toString()));
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        currentLocationMarker = this.googleMap.addMarker(new MarkerOptions().position(latLng));
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition
                .builder()
                .target(targetLocationMarker.getPosition())
                .bearing(MathUtils.bearing(currentLocationMarker.getPosition(), targetLocationMarker.getPosition()))
                .build()));

        LatLngBounds latLngBounds = LatLngBounds.builder().include(targetLocationMarker.getPosition()).include(currentLocationMarker.getPosition()).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(latLngBounds, CAMERA_PADDING);
        googleMap.animateCamera(cameraUpdate);
    }


    public void onRoute(View view) {
        if (polylineRoute != null) {
            Log.i(TAG, "removing route");
            polylineRoute.remove();
        }
        drawRoutes();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void drawRoutes() {
        ArrayList<LatLng> waypointsLatLng = new ArrayList<>();
        for (Marker marker : waypoints)
            waypointsLatLng.add(marker.getPosition());

        googleDirectionsService.getRoutes(currentLocationMarker.getPosition(), targetLocationMarker.getPosition(), waypointsLatLng,
                new Callback<Routes>() {
                    @Override
                    public void onResponse(Call<Routes> call, Response<Routes> response) {
                        Log.i(TAG, "response with routes received");
                        drawRoute(response.body().getRoutes().get(0));

                        LatLng southWest = response.body().getRoutes().get(0).getBounds().getSouthWest().toLatLng();
                        LatLng northEast = response.body().getRoutes().get(0).getBounds().getNorthEast().toLatLng();

                        LatLngBounds.Builder latLngBoundsBuilder = new LatLngBounds.Builder().include(southWest);
                        latLngBoundsBuilder.include(northEast);
                        LatLngBounds latLngBounds = latLngBoundsBuilder.build();

                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(latLngBounds, CAMERA_PADDING);
                        googleMap.animateCamera(cameraUpdate);

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


    private void drawRoute(Route route) {
        if (route != null) {
            Log.i(TAG, "drawing route");
            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions = polylineOptions.addAll(PolyUtil.decode(route.getOverviewPolyline().getPoints()))
                    .width(20)
                    .color(R.color.teal)
                    .clickable(false);
            polylineRoute = googleMap.addPolyline(polylineOptions);
        } else {
            Log.i(TAG, "no route returned for drawing");
        }
    }
}
