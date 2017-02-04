package com.ede.standyourground.framework;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by Eddie on 2/4/2017.
 */

public class UpdateLoopManager {

    private static final UpdateLoopManager instance = new UpdateLoopManager();
    private Handler handler;

    private UpdateLoopManager() {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                UpdateLoopTask updateLoopTask = (UpdateLoopTask) message.obj;
            }
        };
    }

    public static UpdateLoopManager getInstance() {
        return instance;
    }

    public void handle(UpdateLoopTask updateLoopTask) {
        Message message = handler.obtainMessage(0, updateLoopTask);
        message.sendToTarget();
    }
}
