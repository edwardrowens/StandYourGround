package com.ede.standyourground.app.activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ede.standyourground.R;
import com.ede.standyourground.app.service.FindMatchService;
import com.ede.standyourground.app.service.RemoveFromMatchMakingService;
import com.ede.standyourground.app.to.FindMatchRequestTO;
import com.ede.standyourground.app.to.FindMatchResponseTO;
import com.ede.standyourground.framework.Callback;
import com.ede.standyourground.framework.Logger;
import com.ede.standyourground.framework.Receiver;
import com.ede.standyourground.framework.StandYourGroundResultReceiver;
import com.ede.standyourground.networking.framework.NetworkingManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;

public class FindMatchActivity extends AppCompatActivity implements Receiver, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static String FIND_MATCH_RESULT_RECEIVER = FindMatchActivity.class.getName() + ".findMatchResultReceiver";
    public static String PLAYER_ID = FindMatchActivity.class.getName() + ".playerId";
    public static String OPPONENT_LOCATION = FindMatchActivity.class.getName() + ".opponent";
    public static String PLAYER_LOCATION = FindMatchActivity.class.getName() + ".location";
    public static String GAME_SESSION_ID = FindMatchActivity.class.getName() + ".gameSessionId";

    private static final long LOCATION_INTERVAL = 5000;

    private static Logger logger = new Logger(FindMatchActivity.class);

    private StandYourGroundResultReceiver standYourGroundResultReceiver = new StandYourGroundResultReceiver(new Handler());
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location currentLocation;
    private UUID playerId;
    private boolean playerMatched = false;
    private final Handler handler = new Handler(Looper.getMainLooper());

    private Button onFindMatchButton;
    private ProgressBar findingMatchProgressBar;
    private TextView findingMatchText;
    private TextView opponentFoundText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_match);

        onFindMatchButton = (Button) findViewById(R.id.onFindMatchButton);
        findingMatchProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        findingMatchText = (TextView) findViewById(R.id.findingMatchText);
        opponentFoundText = (TextView) findViewById(R.id.opponentFoundText);

        standYourGroundResultReceiver.setReceiver(this);

        playerId = UUID.randomUUID();
        getLocation();
    }

    public void onFindMatch(View view) {
        logger.i("Search button clicked!");

        findingMatchText.setVisibility(View.VISIBLE);
        findingMatchProgressBar.setVisibility(View.VISIBLE);
        onFindMatchButton.setVisibility(View.INVISIBLE);

        Intent intent = new Intent(this, FindMatchService.class);
        intent.putExtra(FindMatchActivity.FIND_MATCH_RESULT_RECEIVER, standYourGroundResultReceiver);

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
        logger.i("Received result from FindMatchService");

        switch(resultCode) {
            case 503:
                logger.e("Received a 503 when looking for opponent");
                resetActivity(getResources().getString(R.string.server_is_down));
                break;
            case 200:
                final FindMatchResponseTO findMatchResponseTO = (FindMatchResponseTO) resultData.get(FindMatchService.FIND_MATCH_RESPONSE);
                connectToOpponent(findMatchResponseTO);
                break;
            default:
                resetActivity(getResources().getString(R.string.problem_connecting));
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
    protected void onDestroy() {
        FindMatchService.stopThread();
        if (!playerMatched) {
            logger.d("Making call to remove player from match making");
            Intent intent = new Intent(this, RemoveFromMatchMakingService.class);
            intent.putExtra(PLAYER_ID, playerId);
            startService(intent);
            logger.d("Call made to remove player from match making");
        }
        super.onDestroy();
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
    public void onLocationChanged(Location location) {
        currentLocation = location;
        onFindMatchButton.setEnabled(true);
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

    private void animateMessage(String message) {
        opponentFoundText.setText(message);
        final Animation in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(1000);

        final Animation out = new AlphaAnimation(1.0f, 0.0f);
        out.setDuration(1000);
        out.setStartOffset(2000);

        in.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                opponentFoundText.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                opponentFoundText.startAnimation(out);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                opponentFoundText.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        opponentFoundText.startAnimation(in);
    }

    private void connectToOpponent(final FindMatchResponseTO findMatchResponseTO) {
        if (findMatchResponseTO != null) {
            logger.i("Connecting to opponent");

            animateMessage(getResources().getString(R.string.find_match_opponent_found));
            findingMatchText.setText(R.string.find_match_connecting);

            playerMatched = true;
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);

            NetworkingManager.getInstance().connect(findMatchResponseTO.getGameSessionId(), new Callback() {
                @Override
                public void onSuccess() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            LatLng opponentLocation = new LatLng(findMatchResponseTO.getLat(), findMatchResponseTO.getLng());

                            Intent intent = new Intent(FindMatchActivity.this, MapsActivity.class);
                            intent.putExtra(OPPONENT_LOCATION, opponentLocation);
                            intent.putExtra(PLAYER_LOCATION, new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
                            intent.putExtra(GAME_SESSION_ID, findMatchResponseTO.getGameSessionId());
                            FindMatchActivity.this.startActivity(intent);
                        }
                    });
                }

                @Override
                public void onFail() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            resetActivity(getResources().getString(R.string.find_match_opponent_lost));
                        }
                    });
                }
            });
        } else {
            logger.w("Received null opponent player in response");
        }
    }

    private void resetActivity(String messageText) {
        logger.e("Could not connect to opponent.");
        animateMessage(messageText);
        if (!playerMatched) {
            FindMatchService.stopThread();
            logger.d("Making call to remove player from match making");
            Intent intent = new Intent(FindMatchActivity.this, RemoveFromMatchMakingService.class);
            intent.putExtra(PLAYER_ID, playerId);
            startService(intent);
        }

        findingMatchText.setVisibility(View.INVISIBLE);
        findingMatchProgressBar.setVisibility(View.INVISIBLE);
        onFindMatchButton.setVisibility(View.VISIBLE);
    }
}
