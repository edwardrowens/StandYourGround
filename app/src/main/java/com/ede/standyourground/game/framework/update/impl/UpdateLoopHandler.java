package com.ede.standyourground.game.framework.update.impl;

import android.os.Handler;
import android.os.Message;

import com.ede.standyourground.game.model.MovableUnit;
import com.ede.standyourground.game.model.Unit;

public class UpdateLoopHandler extends Handler {

//    private static Logger logger = new Logger(UpdateLoopHandler.class);

    @Override
    public void handleMessage(Message message) {
        UpdateLoopTask updateLoopTask = (UpdateLoopTask) message.obj;
        for (Unit unit : updateLoopTask.getUpdatedUnits()) {
            if (unit instanceof MovableUnit) {
            }
        }
    }
}
