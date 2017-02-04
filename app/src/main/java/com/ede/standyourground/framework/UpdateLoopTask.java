package com.ede.standyourground.framework;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.ede.standyourground.game.model.MovableUnit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eddie on 2/4/2017.
 */

public class UpdateLoopTask {
    private Logger logger = new Logger(UpdateLoopTask.class);

    private static final Handler handler = new Handler(Looper.getMainLooper());
    private List<MovableUnit> updatedUnits = new ArrayList<>();

    public List<MovableUnit> getUpdatedUnits() {
        return updatedUnits;
    }

    public void setUpdatedUnits(List<MovableUnit> updatedUnits) {
        this.updatedUnits = updatedUnits;
    }

    public void send() {
        Message message = handler.obtainMessage(0, this);
        logger.i("target is %s", message.getTarget().toString());
        message.sendToTarget();
    }
}
