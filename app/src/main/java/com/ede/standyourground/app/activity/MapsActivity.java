package com.ede.standyourground.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;

import com.ede.standyourground.R;
import com.ede.standyourground.app.service.android.StopGameService;
import com.ede.standyourground.app.service.api.DrawRouteService;
import com.ede.standyourground.app.service.api.GameEndService;
import com.ede.standyourground.app.service.api.OnMapReadyService;
import com.ede.standyourground.app.ui.Component;
import com.ede.standyourground.app.ui.HealthBarComponent;
import com.ede.standyourground.framework.Logger;
import com.ede.standyourground.framework.api.LatLngService;
import com.ede.standyourground.framework.dagger.application.MyApp;
import com.ede.standyourground.framework.dagger.providers.GameSessionIdProvider;
import com.ede.standyourground.framework.dagger.providers.GoogleMapProvider;
import com.ede.standyourground.game.framework.management.impl.WorldManager;
import com.ede.standyourground.game.model.Units;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private final Logger logger = new Logger(MapsActivity.class);

    // VIEWS
    private HorizontalScrollView unitChoicesScrollView;
    private Button confirmRouteButton;

    private GoogleMap googleMap;
    private List<Marker> waypoints = new ArrayList<>();
    private Units selectedUnit;
    private LatLng playerLocation;
    private LatLng opponentLocation;
    private static Map<Class<? extends Component>, Component> componentMap = new ConcurrentHashMap<>();

    @Inject WorldManager worldManager;
    @Inject LatLngService latLngService;
    @Inject GoogleMapProvider googleMapProvider;
    @Inject GameSessionIdProvider gameSessionIdProvider;
    @Inject OnMapReadyService onMapReadyService;
    @Inject GameEndService gameEndService;
    @Inject DrawRouteService drawRouteService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);

        HealthBarComponent healthBarComponent = new HealthBarComponent(this);
        componentMap.put(HealthBarComponent.class, healthBarComponent);

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
    protected void onDestroy() {
        worldManager.stop();
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
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));

        onMapReadyService.onMapReady(opponentLocation, playerLocation);
        gameEndService.registerEndGame(this);

        confirmRouteButton = (Button) findViewById(R.id.confirmRouteButton);
        unitChoicesScrollView = (HorizontalScrollView) findViewById(R.id.unitChoicesScrollView);
    }

    public void onRoute(View view) {
        logger.i("On route clicked");
        drawRouteService.drawRoutesForUnit(selectedUnit, waypoints, playerLocation, opponentLocation);

        // TODO DELETE
        logger.i("Creating enemy foot soldier.");
        drawRouteService.drawRoutesForEnemyUnit(Units.FOOT_SOLDIER, new ArrayList<Marker>(), playerLocation, opponentLocation);
        // TODO END OF DELETE

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
            }
        });

        for (Marker marker : waypoints) {
            marker.remove();
        }
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
            case R.id.marauder:
                selectedUnit = Units.MARAUDER;
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

    public static Component getComponent(Class<? extends Component> componentClass) {
        return componentMap.get(componentClass);
    }
}
