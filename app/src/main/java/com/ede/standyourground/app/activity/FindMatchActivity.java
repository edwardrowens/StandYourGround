package com.ede.standyourground.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ede.standyourground.R;
import com.ede.standyourground.app.activity.service.FindMatchService;
import com.ede.standyourground.app.activity.service.RemoveFromMatchMakingService;
import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.framework.api.dagger.application.MyApp;
import com.ede.standyourground.framework.api.service.LatLngService;
import com.ede.standyourground.framework.api.transmit.Callback;
import com.ede.standyourground.framework.api.transmit.Receiver;
import com.ede.standyourground.framework.api.transmit.StandYourGroundResultReceiver;
import com.ede.standyourground.game.api.model.GameMode;
import com.ede.standyourground.networking.api.NetworkingHandler;
import com.ede.standyourground.networking.api.exchange.payload.request.FindMatchRequest;
import com.ede.standyourground.networking.api.exchange.payload.response.FindMatchResponse;
import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;

import javax.inject.Inject;

import retrofit2.Retrofit;

public class FindMatchActivity extends AppCompatActivity implements Receiver {

    private static Logger logger = new Logger(FindMatchActivity.class);

    public static String FIND_MATCH_RESULT_RECEIVER = FindMatchActivity.class.getName() + ".findMatchResultReceiver";
    public static String PLAYER_ID = FindMatchActivity.class.getName() + ".playerId";
    public static String PLAYER_LOCATION = FindMatchActivity.class.getName() + ".playerLocation";
    public static String ENEMY_SEARCH_RADIUS = FindMatchActivity.class.getName() + ".enemySearchRadius";
    public static String GAME_SESSION_ID = FindMatchActivity.class.getName() + ".gameSessionId";

    private StandYourGroundResultReceiver standYourGroundResultReceiver = new StandYourGroundResultReceiver(new Handler());
    private UUID playerId;
    private LatLng playerLocation;
    private int enemySearchRadius;
    private boolean playerMatched = false;
    private final Handler handler = new Handler(Looper.getMainLooper());

    private Button onFindMatchButton;
    private ProgressBar findingMatchProgressBar;
    private TextView findingMatchText;
    private TextView opponentFoundText;

    @Inject
    LatLngService latLngService;
    @Inject
    NetworkingHandler networkingHandler;
    @Inject
    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApp.getAppComponent().inject(this);
        setContentView(R.layout.activity_find_match);

        playerLocation = (LatLng) getIntent().getExtras().get(PLAYER_LOCATION);
        enemySearchRadius = (int) getIntent().getExtras().get(ENEMY_SEARCH_RADIUS);

        onFindMatchButton = (Button) findViewById(R.id.onFindMatchButton);
        findingMatchProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        findingMatchText = (TextView) findViewById(R.id.findingMatchText);
        opponentFoundText = (TextView) findViewById(R.id.opponentFoundText);

        standYourGroundResultReceiver.setReceiver(this);
        playerId = UUID.randomUUID();
    }

    public void onFindMatch(View view) {
        logger.i("Search button clicked!");

        findingMatchText.setVisibility(View.VISIBLE);
        findingMatchProgressBar.setVisibility(View.VISIBLE);
        onFindMatchButton.setVisibility(View.INVISIBLE);

        Intent intent = new Intent(this, FindMatchService.class);
        intent.putExtra(FindMatchActivity.FIND_MATCH_RESULT_RECEIVER, standYourGroundResultReceiver);

        FindMatchRequest findMatchRequest = new FindMatchRequest();
        findMatchRequest.setId(playerId);
        findMatchRequest.setLat(playerLocation.latitude);
        findMatchRequest.setLng(playerLocation.longitude);
        findMatchRequest.setRadius((int) (enemySearchRadius * 1.60934));

        intent.putExtra(FindMatchService.FIND_MATCH_REQUEST, findMatchRequest);

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
                final FindMatchResponse findMatchResponse = (FindMatchResponse) resultData.get(FindMatchService.FIND_MATCH_RESPONSE);
                connectToOpponent(findMatchResponse);
                break;
            default:
                resetActivity(getResources().getString(R.string.problem_connecting));
        }
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

    private void connectToOpponent(final FindMatchResponse findMatchResponse) {
        if (findMatchResponse != null) {
            logger.i("Connecting to opponent");

            animateMessage(getResources().getString(R.string.find_match_opponent_found));
            findingMatchText.setText(R.string.find_match_connecting);

            playerMatched = true;

            networkingHandler.connect(findMatchResponse.getGameSessionId(), new Callback() {
                @Override
                public void onSuccess() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            LatLng opponentLocation = new LatLng(findMatchResponse.getLat(), findMatchResponse.getLng());

                            Intent intent = new Intent(FindMatchActivity.this, MapsActivity.class);
                            intent.putExtra(MapsActivity.OPPONENT_LOCATION, opponentLocation);
                            intent.putExtra(MapsActivity.PLAYER_LOCATION, playerLocation);
                            intent.putExtra(MapsActivity.GAME_SESSION_ID, findMatchResponse.getGameSessionId());
                            intent.putExtra(MapsActivity.GAME_MODE, GameMode.MULTIPLAYER);
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
