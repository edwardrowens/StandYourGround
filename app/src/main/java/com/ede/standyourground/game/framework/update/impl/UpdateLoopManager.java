package com.ede.standyourground.game.framework.update.impl;

import android.os.Handler;
import android.os.Message;

import com.ede.standyourground.framework.Logger;


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
