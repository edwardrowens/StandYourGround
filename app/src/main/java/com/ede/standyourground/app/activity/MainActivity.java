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
import com.ede.standyourground.framework.Logger;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final Logger logger = new Logger(MainActivity.class);
    private static final int FINE_LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Point point = new Point();
//        point.x = 100;
//        point.y = 100;
//
//        UnitGroupComponent unitGroupComponent = new UnitGroupComponent(this, point);
//        unitGroupComponent.createUnitGroupBlockCount(Units.BASE, 2);
//        unitGroupComponent.initialize(R.id.activity_main);
//        unitGroupComponent.drawComponentElements();

//        List<UUID> id = new ArrayList<>();
//        id.add(UUID.randomUUID());
//        HealthBar healthBar = new HealthBar(id.get(0), this);
//        healthBar.setHealthPercentage(1f);
//        healthBar.setWidth(100);
//        healthBar.setHeight(50);
//        PointF pointF = new PointF();
//        pointF.x = 0f;
//        pointF.y = 0f;
//        healthBar.setPoint(pointF);
//        UnitGroupBlockHealthBar unitGroupBlockHealthBar = new UnitGroupBlockHealthBar(UUID.randomUUID(), id, this, Units.FOOT_SOLDIER, healthBar);
//
//        ((RelativeLayout) findViewById(R.id.activity_main)).addView(unitGroupBlockHealthBar.getContainer());
//        logger.d("setting health percent");
//        healthBar.setHealthPercentage(.5f);
//        healthBar.setHealthPercentage(.5f);
//        healthBar.setHealthPercentage(.5f);
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


    public void startGame(View view) {
        logger.i("Starting game");
        Intent intent = new Intent(this, FindMatchActivity.class);
        startActivity(intent);
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
}
