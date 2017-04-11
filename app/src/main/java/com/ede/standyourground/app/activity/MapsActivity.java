package com.ede.standyourground.app.activity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ede.standyourground.R;
import com.ede.standyourground.app.activity.service.StopGameService;
import com.ede.standyourground.app.event.OnCameraMoveListenerFactory;
import com.ede.standyourground.app.event.OnCircleClickListenerFactory;
import com.ede.standyourground.app.event.OnMapLoadedCallbackFactory;
import com.ede.standyourground.app.ui.api.component.Component;
import com.ede.standyourground.app.ui.api.component.HealthBarComponentFactory;
import com.ede.standyourground.app.ui.api.component.UnitChoicesMenuComponentFactory;
import com.ede.standyourground.app.ui.api.event.ConfirmRouteListener;
import com.ede.standyourground.app.ui.api.event.RouteCancelListener;
import com.ede.standyourground.app.ui.api.event.UnitSelectedListener;
import com.ede.standyourground.app.ui.api.service.UnitChoicesMenuService;
import com.ede.standyourground.app.ui.impl.component.NeutralCampListingComponent;
import com.ede.standyourground.app.ui.impl.component.UnitChoicesMenuComponent;
import com.ede.standyourground.app.ui.impl.component.UnitGroupComponent;
import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.framework.api.dagger.application.MyApp;
import com.ede.standyourground.framework.api.dagger.providers.GameSessionIdProvider;
import com.ede.standyourground.framework.api.dagger.providers.GoogleMapProvider;
import com.ede.standyourground.framework.api.service.DrawRouteService;
import com.ede.standyourground.framework.api.service.GraphicService;
import com.ede.standyourground.game.api.event.listener.BankNeutralCampIncomeListener;
import com.ede.standyourground.game.api.event.listener.CoinBalanceChangeListener;
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
import com.ede.standyourground.game.api.service.PlayerService;
import com.ede.standyourground.game.api.service.UnitService;
import com.ede.standyourground.game.impl.model.BankNeutralCamp;
import com.ede.standyourground.game.impl.model.MedicNeutralCamp;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
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

    public static Resources resources;

    // Listeners
    private GoogleMap.OnCircleClickListener onCircleClickListener;

    // VIEWS
    private TextView coins;

    private static GoogleMap googleMap;
    private List<Marker> waypoints = new ArrayList<>();
    private LatLng playerLocation;
    private LatLng opponentLocation;
    private static final Map<Class<? extends Component>, Component> componentMap = new ConcurrentHashMap<>();
    private static final Map<UUID, Circle> circles = new ConcurrentHashMap<>();

    @Inject
    GoogleMapProvider googleMapProvider;
    @Inject
    GameSessionIdProvider gameSessionIdProvider;
    @Inject
    DrawRouteService drawRouteService;
    @Inject
    GameService gameService;
    @Inject
    UnitService unitService;
    @Inject
    OnCircleClickListenerFactory onCircleClickListenerFactory;
    @Inject
    HealthBarComponentFactory healthBarComponentFactory;
    @Inject
    OnMapLoadedCallbackFactory onMapLoadedCallbackFactory;
    @Inject
    OnCameraMoveListenerFactory onCameraMoveListenerFactory;
    @Inject
    PlayerService playerService;
    @Inject
    GraphicService graphicService;
    @Inject
    UnitChoicesMenuComponentFactory unitChoicesMenuComponentFactory;
    @Inject
    UnitChoicesMenuService unitChoicesMenuService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApp.getAppComponent().inject(this);

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        resources = getResources();

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
        googleMap.getUiSettings().setAllGesturesEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setCompassEnabled(false);

        UnitGroupComponent unitGroupComponent = new UnitGroupComponent(this, new Point(0, 0));
        NeutralCampListingComponent neutralCampListingComponent = new NeutralCampListingComponent(this, new Point(0, 0), "");
        final UnitChoicesMenuComponent unitChoicesMenuComponent = unitChoicesMenuComponentFactory.createUnitChoicesMenuComponent(this, (ViewGroup) findViewById(R.id.mapContainer));

        componentMap.put(UnitGroupComponent.class, unitGroupComponent);
        componentMap.put(NeutralCampListingComponent.class, neutralCampListingComponent);
        componentMap.put(UnitChoicesMenuComponent.class, unitChoicesMenuComponent);

        // Create map listeners
        onCircleClickListener = onCircleClickListenerFactory.createOnCircleClickedListener(unitGroupComponent, neutralCampListingComponent, unitChoicesMenuComponent);
        googleMap.setOnCameraMoveListener(onCameraMoveListenerFactory.createOnCameraMoveListener(unitGroupComponent,  neutralCampListingComponent, unitChoicesMenuComponent));
        googleMap.setOnMapLoadedCallback(onMapLoadedCallbackFactory.createOnMapLoadedCallback(playerLocation, opponentLocation, unitGroupComponent, neutralCampListingComponent));
        googleMap.setOnCircleClickListener(onCircleClickListener);

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
                            unitChoicesMenuService.setVisibility(unitChoicesMenuComponent, Units.MEDIC, View.VISIBLE);
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

        gameService.registerCoinBalanceChangeListener(new CoinBalanceChangeListener() {
            @Override
            public void onCoinBalanceChange(final int oldBalance, final int newBalance) {
                MapsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ValueAnimator valueAnimator = new ValueAnimator();
                        valueAnimator.setObjectValues(oldBalance, newBalance);
                        valueAnimator.setDuration(500);
                        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                coins.setText(Integer.toString((int) animation.getAnimatedValue()));
                            }
                        });
                        valueAnimator.start();
                    }
                });
            }
        });

        unitService.registerBankNeutralCampIncomeListener(new BankNeutralCampIncomeListener() {
            @Override
            public void onBankNeutralCampIncome(final BankNeutralCamp bank) {
                MapsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Projection projection = googleMap.getProjection();
                        final Point unitCenter = projection.toScreenLocation(bank.getStartingPosition());
                        String text = Integer.toString(bank.getProvidedIncome());

                        final TextView textView = new TextView(MapsActivity.this);
                        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.topMargin = unitCenter.y;
                        layoutParams.leftMargin = unitCenter.x;
                        textView.setLayoutParams(layoutParams);
                        textView.setText(text);
                        textView.setTypeface(null, Typeface.BOLD);
                        textView.setTextColor(getResources().getColor(R.color.cast_expanded_controller_background_color));
                        Rect rect = new Rect();
                        textView.getPaint().getTextBounds(text, 0, text.length(), rect);
                        float x = unitCenter.x - rect.width() / 2;
                        float y = unitCenter.y - rect.height() * 2;
                        Animation animation = new TranslateAnimation(x, x, y, y - rect.height());
                        animation.setDuration(1000);
                        animation.setFillAfter(true);
                        textView.setAnimation(animation);
                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                ((ViewGroup) textView.getParent()).removeView(textView);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        textView.animate();

                        ((ViewGroup) findViewById(R.id.mapContainer)).addView(textView);
                    }
                });
            }
        });

        unitChoicesMenuComponent.registerUnitSelectedListener(new UnitSelectedListener() {
            @Override
            public void onUnitSelected(Units units) {
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        waypoints.add(googleMap.addMarker(markerOptions));
                    }
                });
            }
        });

        unitChoicesMenuComponent.registerRouteCancelListener(new RouteCancelListener() {
            @Override
            public void onRouteCancelled() {
                for (Marker marker : waypoints) {
                    marker.remove();
                }
                waypoints.clear();
            }
        });

        unitChoicesMenuComponent.registerConfirmRouteListener(new ConfirmRouteListener() {
            @Override
            public void onConfirmRoute(Units selectedUnit) {
                logger.i("On route clicked");
                playerService.makePurchase(selectedUnit.getCost());
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
            }
        });

        coins = (TextView) findViewById(R.id.coins);

        LinearLayout resourcesLayout = (LinearLayout) findViewById(R.id.resourcesLayout);
        resourcesLayout.setZ(1f);
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
