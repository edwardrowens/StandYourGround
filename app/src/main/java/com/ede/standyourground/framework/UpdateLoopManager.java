package com.ede.standyourground.framework;

import android.os.Handler;
import android.os.Message;

/**
 * Created by Eddie on 2/4/2017.
 */

public class UpdateLoopManager {

    private static Logger logger = new Logger(UpdateLoopManager.class);
    private static final UpdateLoopManager instance = new UpdateLoopManager();
    private static Handler handler;

    private UpdateLoopManager() {
    }

    public static void setHandler(Handler handler) {
        if (UpdateLoopManager.handler == null )
            UpdateLoopManager.handler = handler;
    }

    public static UpdateLoopManager getInstance() {
        return instance;
    }

    public void handle(UpdateLoopTask updateLoopTask) {
        Message message = handler.obtainMessage(0, updateLoopTask);
        message.sendToTarget();
    }
}
