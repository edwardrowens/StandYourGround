package com.ede.standyourground.app.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ede.standyourground.R;
import com.ede.standyourground.app.ui.api.component.UnitGroupBlockCountComponentFactory;
import com.ede.standyourground.app.ui.api.component.UnitGroupBlockHealthBarComponentFactory;
import com.ede.standyourground.app.ui.api.component.UnitGroupComponentFactory;
import com.ede.standyourground.app.ui.api.service.UnitGroupComponentService;
import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.framework.api.dagger.application.MyApp;
import com.ede.standyourground.framework.api.dagger.providers.GameModeProvider;
import com.ede.standyourground.game.api.model.GameMode;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final Logger logger = new Logger(MainActivity.class);
    private static final int FINE_LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Inject
    GameModeProvider gameModeProvider;

    @Inject
    UnitGroupBlockCountComponentFactory unitGroupBlockCountComponentFactory;

    @Inject
    UnitGroupComponentFactory unitGroupComponentFactory;

    @Inject
    UnitGroupComponentService unitGroupComponentService;
    @Inject
    UnitGroupBlockHealthBarComponentFactory unitGroupBlockHealthBarComponentFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApp.getAppComponent().inject(this);
        setContentView(R.layout.activity_main);

        requestPermissions();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case FINE_LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    logger.i("Access fine location granted");
                } else {
                    logger.i("Access fine location denied");
                }
            }
        }
    }


    public void startMultiplayerGame(View view) {
        logger.i("Starting multiplayer game");
        gameModeProvider.setGameMode(GameMode.MULTIPLAYER);
        Intent intent = new Intent(this, SelectLocationActivity.class);
        intent.putExtra(SelectLocationActivity.GAME_MODE, GameMode.MULTIPLAYER);
        startActivity(intent);
    }

    public void startSinglePlayerGame(View view) {
        logger.i("Starting single player game");
        gameModeProvider.setGameMode(GameMode.SINGLE_PLAYER);
        Intent intent = new Intent(this, SelectLocationActivity.class);
        intent.putExtra(SelectLocationActivity.GAME_MODE, GameMode.SINGLE_PLAYER);
        startActivity(intent);
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
}
