package com.ede.standyourground.app.activity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.Toast;

import com.ede.standyourground.R;
import com.ede.standyourground.app.service.android.StopGameService;
import com.ede.standyourground.app.service.api.DrawRouteService;
import com.ede.standyourground.app.service.api.MapSetupService;
import com.ede.standyourground.app.ui.Component;
import com.ede.standyourground.app.ui.HealthBarComponent;
import com.ede.standyourground.app.ui.UnitGroupComponent;
import com.ede.standyourground.framework.Logger;
import com.ede.standyourground.framework.dagger.application.MyApp;
import com.ede.standyourground.framework.dagger.providers.GameSessionIdProvider;
import com.ede.standyourground.framework.dagger.providers.GoogleMapProvider;
import com.ede.standyourground.game.framework.management.api.GameService;
import com.ede.standyourground.game.framework.management.api.UnitService;
import com.ede.standyourground.game.model.Unit;
import com.ede.standyourground.game.model.Units;
import com.ede.standyourground.game.model.api.DeathListener;
import com.ede.standyourground.game.model.api.GameEndListener;
import com.ede.standyourground.game.model.api.UnitCreatedListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
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

    private static final Logger logger = new Logger(MapsActivity.class);

    // VIEWS
    private HorizontalScrollView unitChoicesScrollView;
    private Button confirmRouteButton;

    private static GoogleMap googleMap;
    private List<Marker> waypoints = new ArrayList<>();
    private Units selectedUnit;
    private LatLng playerLocation;
    private LatLng opponentLocation;
    private static final Map<Class<? extends Component>, Component> componentMap = new ConcurrentHashMap<>();
    private static final Map<UUID, Circle> circles = new ConcurrentHashMap<>();

    @Inject GoogleMapProvider googleMapProvider;
    @Inject GameSessionIdProvider gameSessionIdProvider;
    @Inject MapSetupService mapSetupService;
    @Inject DrawRouteService drawRouteService;
    @Inject GameService gameService;
    @Inject UnitService unitService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApp.getAppComponent().inject(this);

        HealthBarComponent healthBarComponent = new HealthBarComponent(this);
        UnitGroupComponent unitGroupComponent = new UnitGroupComponent(this, new Point(0,0));
        componentMap.put(UnitGroupComponent.class, unitGroupComponent);
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
        gameService.stopGame();
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
        MapsActivity.googleMap = googleMap;

        googleMapProvider.setGoogleMap(googleMap);
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));

        mapSetupService.setupMap(opponentLocation, playerLocation);
        gameService.registerGameEndListener(new GameEndListener() {
            @Override
            public void onGameEnd(final boolean won) {
                MapsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (won) {
                            Toast.makeText(MapsActivity.this, "You Win", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MapsActivity.this, "You Lose", Toast.LENGTH_SHORT).show();
                        }
                        Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                        MapsActivity.this.startActivity(intent);
                    }
                });
            }
        });

        unitService.registerUnitCreatedListener(new UnitCreatedListener() {
            @Override
            public void onUnitCreated(final Unit unit) {
                MapsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (unit.isEnemy()) {
                            int color = MapsActivity.this.getResources().getColor(unit.getType().getEnemyColor());
                            addCircle(unit.getId(), unit.getType().getCircleOptions().center(unit.getCurrentPosition()).fillColor(color));
                        } else {
                            int color = MapsActivity.this.getResources().getColor(unit.getType().getPlayerColor());
                            addCircle(unit.getId(), unit.getType().getCircleOptions().center(unit.getCurrentPosition()).fillColor(color));
                        }
                    }
                });
            }
        });

        unitService.registerDeathListener(new DeathListener() {
            @Override
            public void onDeath(final Unit mortal) {
                MapsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Circle circle = circles.get(mortal.getId());
                        if (circle != null) {
                            removeCircle(mortal.getId());
                        }
                    }
                });
            }
        });

        confirmRouteButton = (Button) findViewById(R.id.confirmRouteButton);
        unitChoicesScrollView = (HorizontalScrollView) findViewById(R.id.unitChoicesScrollView);
    }

    public void onRoute(View view) {
        logger.i("On route clicked");
        drawRouteService.drawRoutesForUnit(selectedUnit, waypoints, playerLocation, opponentLocation);

        // TODO DELETE
        logger.i("Creating enemy foot soldier.");
        drawRouteService.drawRoutesForEnemyUnit(Units.MARAUDER, new ArrayList<Marker>(), playerLocation, opponentLocation);
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

    public static Map<UUID, Circle> getCircles() {
        return circles;
    }

    public static void removeCircle(UUID unitId) {
        Circle circle = circles.get(unitId);
        if (circle == null) {
            logger.w("Attempted to remove circle that had already been removed for unit %s", unitId);
        } else {
            circle.remove();
        }
        circles.remove(unitId);
    }

    private void addCircle(UUID unitId, CircleOptions circleOptions) {
        logger.d("adding %s", unitId);
        circles.put(unitId, googleMap.addCircle(circleOptions));
    }
}
