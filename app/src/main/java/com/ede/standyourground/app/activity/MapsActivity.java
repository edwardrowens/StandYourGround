package com.ede.standyourground.app.activity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.Toast;

import com.ede.standyourground.R;
import com.ede.standyourground.app.activity.service.StopGameService;
import com.ede.standyourground.app.event.OnCameraMoveListenerFactory;
import com.ede.standyourground.app.event.OnCircleClickListenerFactory;
import com.ede.standyourground.app.event.OnMapLoadedCallbackFactory;
import com.ede.standyourground.app.ui.api.component.Component;
import com.ede.standyourground.app.ui.api.component.HealthBarComponentFactory;
import com.ede.standyourground.app.ui.impl.component.HealthBarComponent;
import com.ede.standyourground.app.ui.impl.component.NeutralCampListingComponent;
import com.ede.standyourground.app.ui.impl.component.UnitGroupComponent;
import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.framework.api.dagger.application.MyApp;
import com.ede.standyourground.framework.api.dagger.providers.GameSessionIdProvider;
import com.ede.standyourground.framework.api.dagger.providers.GoogleMapProvider;
import com.ede.standyourground.framework.api.service.DrawRouteService;
import com.ede.standyourground.game.api.event.listener.GameEndListener;
import com.ede.standyourground.game.api.event.listener.OnDeathListener;
import com.ede.standyourground.game.api.event.listener.PositionChangeListener;
import com.ede.standyourground.game.api.event.listener.UnitCreatedListener;
import com.ede.standyourground.game.api.event.listener.VisibilityChangeListener;
import com.ede.standyourground.game.api.model.Hostility;
import com.ede.standyourground.game.api.model.MovableUnit;
import com.ede.standyourground.game.api.model.NeutralCamp;
import com.ede.standyourground.game.api.model.Unit;
import com.ede.standyourground.game.api.model.Units;
import com.ede.standyourground.game.api.service.GameService;
import com.ede.standyourground.game.api.service.UnitService;
import com.ede.standyourground.game.impl.model.MedicNeutralCamp;
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
    private Button medicUnitButton;

    private static GoogleMap googleMap;
    private List<Marker> waypoints = new ArrayList<>();
    private Units selectedUnit;
    private LatLng playerLocation;
    private LatLng opponentLocation;
    private static final Map<Class<? extends Component>, Component> componentMap = new ConcurrentHashMap<>();
    private static final Map<UUID, Circle> circles = new ConcurrentHashMap<>();

    @Inject GoogleMapProvider googleMapProvider;
    @Inject GameSessionIdProvider gameSessionIdProvider;
    @Inject DrawRouteService drawRouteService;
    @Inject GameService gameService;
    @Inject UnitService unitService;
    @Inject OnCircleClickListenerFactory onCircleClickListenerFactory;
    @Inject HealthBarComponentFactory healthBarComponentFactory;
    @Inject OnMapLoadedCallbackFactory onMapLoadedCallbackFactory;
    @Inject OnCameraMoveListenerFactory onCameraMoveListenerFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApp.getAppComponent().inject(this);

        UnitGroupComponent unitGroupComponent = new UnitGroupComponent(this, new Point(0, 0));
        NeutralCampListingComponent neutralCampListingComponent = new NeutralCampListingComponent(this, new Point(0,0), "SUCK MY WEINERRRRRR");
        componentMap.put(UnitGroupComponent.class, unitGroupComponent);
        componentMap.put(HealthBarComponent.class, healthBarComponentFactory.createHealthBarComponent(this));
        componentMap.put(NeutralCampListingComponent.class, neutralCampListingComponent);

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

        // Map setup
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setCompassEnabled(false);

        UnitGroupComponent unitGroupComponent = (UnitGroupComponent) componentMap.get(UnitGroupComponent.class);
        HealthBarComponent healthBarComponent = (HealthBarComponent) componentMap.get(HealthBarComponent.class);
        NeutralCampListingComponent neutralCampListingComponent = (NeutralCampListingComponent) componentMap.get(NeutralCampListingComponent.class);

        // Create map listeners
        googleMap.setOnCameraMoveListener(onCameraMoveListenerFactory.createOnCameraMoveListener(unitGroupComponent, healthBarComponent, neutralCampListingComponent));
        googleMap.setOnMapLoadedCallback(onMapLoadedCallbackFactory.createOnMapLoadedCallback(playerLocation, opponentLocation, unitGroupComponent, neutralCampListingComponent));
        googleMap.setOnCircleClickListener(onCircleClickListenerFactory.createOnCircleClickedListener(unitGroupComponent, neutralCampListingComponent));

        // Register listeners for game events
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
                        // Add circle
                        if (unit.getHostility() == Hostility.NEUTRAL || unit.getHostility() == Hostility.ENEMY) {
                            int color = ContextCompat.getColor(MapsActivity.this, unit.getType().getEnemyColor());
                            addCircle(unit.getId(), unit.getType().getCircleOptions().center(unit.getStartingPosition()).fillColor(color));
                        } else {
                            int color = ContextCompat.getColor(MapsActivity.this, unit.getType().getFriendlyColor());
                            addCircle(unit.getId(), unit.getType().getCircleOptions().center(unit.getStartingPosition()).fillColor(color));
                        }
                        circles.get(unit.getId()).setVisible(unit.isVisible());
                    }
                });
            }
        });

        unitService.registerOnDeathListener(new OnDeathListener() {
            @Override
            public void onDeath(final Unit mortal, final Unit killer) {
                MapsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Circle circle = circles.get(mortal.getId());
                        if (circle != null) {
                            removeCircle(mortal.getId());
                        }
                        if (mortal instanceof MedicNeutralCamp && killer.getHostility() == Hostility.FRIENDLY) {
                            medicUnitButton.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });

        unitService.registerVisibilityChangeListener(new VisibilityChangeListener() {
            @Override
            public void onVisibilityChange(final Unit unit) {
                MapsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (unit instanceof NeutralCamp) {
                            if (unit.isVisible()) {
                                circles.get(unit.getId()).setVisible(true);
                            }
                        } else {
                            circles.get(unit.getId()).setVisible(unit.isVisible());
                        }
                    }
                });
            }
        });

        unitService.registerPositionChangeListener(new PositionChangeListener() {
            @Override
            public void onPositionChange(final MovableUnit movableUnit) {
                MapsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        circles.get(movableUnit.getId()).setCenter(movableUnit.getCurrentPosition());
                    }
                });
            }
        });

        gameService.startGame();

        confirmRouteButton = (Button) findViewById(R.id.confirmRouteButton);
        unitChoicesScrollView = (HorizontalScrollView) findViewById(R.id.unitChoicesScrollView);
        medicUnitButton = (Button) findViewById(R.id.medicButton);
    }

    public void onRoute(View view) {
        logger.i("On route clicked");
        drawRouteService.drawRoutesForUnit(selectedUnit, waypoints, playerLocation, opponentLocation);

        // TODO DELETE
        logger.i("Creating enemy unit.");
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
        UnitGroupComponent unitGroupComponent = (UnitGroupComponent) componentMap.get(UnitGroupComponent.class);
        NeutralCampListingComponent neutralCampListingComponent = (NeutralCampListingComponent) componentMap.get(NeutralCampListingComponent.class);
        googleMap.setOnCircleClickListener(onCircleClickListenerFactory.createOnCircleClickedListener(unitGroupComponent, neutralCampListingComponent));
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
            case R.id.medicButton:
                selectedUnit = Units.MEDIC;
                break;
        }

        googleMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {
            @Override
            public void onCircleClick(Circle circle) {

            }
        });

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                waypoints.add(googleMap.addMarker(markerOptions));
            }
        });
    }

    private void removeCircle(UUID unitId) {
        Circle circle = circles.get(unitId);
        if (circle == null) {
            logger.w("Attempted to remove circle that had already been removed for unit %s", unitId);
        } else {
            logger.i("Removing circle for unit %s", unitId);
            circle.remove();
        }
        circles.remove(unitId);
    }

    private void addCircle(UUID unitId, CircleOptions circleOptions) {
        logger.d("Creating circle for unit %s", unitId);
        circles.put(unitId, googleMap.addCircle(circleOptions));
    }
}
