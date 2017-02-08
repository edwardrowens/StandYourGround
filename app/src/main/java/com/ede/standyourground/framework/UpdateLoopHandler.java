package com.ede.standyourground.framework;

import android.os.Handler;
import android.os.Message;

import com.ede.standyourground.game.model.MovableUnit;
import com.ede.standyourground.game.model.Unit;

/**
 * Created by Eddie on 2/7/2017.
 */

public class UpdateLoopHandler extends Handler {

//    private static Logger logger = new Logger(UpdateLoopHandler.class);

    @Override
    public void handleMessage(Message message) {
        UpdateLoopTask updateLoopTask = (UpdateLoopTask) message.obj;
        for (Unit unit : updateLoopTask.getUpdatedUnits()) {
            if (unit instanceof MovableUnit) {
                MovableUnit movableUnit = (MovableUnit) WorldManager.getInstance().getUnit(unit.getId());
                movableUnit.getMarker().setPosition(movableUnit.getPosition());
            }
        }
    }
}
