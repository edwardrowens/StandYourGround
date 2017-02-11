package com.ede.standyourground.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ede.standyourground.R;
import com.ede.standyourground.app.service.FindMatchService;
import com.ede.standyourground.framework.Logger;
import com.ede.standyourground.framework.Receiver;
import com.ede.standyourground.framework.StandYourGroundResultReceiver;

public class FindMatchActivity extends AppCompatActivity implements Receiver {

    public static String FIND_MATCH_RESULT_RECEIVER = FindMatchActivity.class.getName() +".findMatchResultReceiver";
    public static String OPPONENT = FindMatchActivity.class.getName() +".opponent";

    private static Logger logger = new Logger(FindMatchActivity.class);

    private StandYourGroundResultReceiver standYourGroundResultReceiver = new StandYourGroundResultReceiver(new Handler());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_match);

        standYourGroundResultReceiver.setReceiver(this);
    }

    public void onSearch(View view) {
        logger.i("Search button clicked!");
        Intent intent = new Intent(this, FindMatchService.class);
        intent.putExtra(FindMatchActivity.FIND_MATCH_RESULT_RECEIVER, standYourGroundResultReceiver);

        //TODO create the request!

        this.startService(intent);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        logger.i("Match found!");
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra(OPPONENT, resultData);
        this.startActivity(intent);
    }
}
