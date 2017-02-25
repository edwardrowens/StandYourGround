package com.ede.standyourground.framework;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class StandYourGroundResultReceiver extends ResultReceiver {
    private Receiver mReceiver;

    public StandYourGroundResultReceiver(Handler handler) {
        super(handler);
    }

    public void setReceiver(Receiver receiver) {
        mReceiver = receiver;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {

        if (mReceiver != null) {
            mReceiver.onReceiveResult(resultCode, resultData);
        }
    }
}
