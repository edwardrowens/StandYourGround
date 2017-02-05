package com.ede.standyourground.framework;

import com.ede.standyourground.game.model.MovableUnit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eddie on 2/4/2017.
 */

public class UpdateLoopTask {
    private Logger logger = new Logger(UpdateLoopTask.class);

    private List<MovableUnit> updatedUnits = new ArrayList<>();
    public List<MovableUnit> getUpdatedUnits() {
        return updatedUnits;
    }

    public void setUpdatedUnits(List<MovableUnit> updatedUnits) {
        this.updatedUnits = updatedUnits;
    }


    public void send() {
        UpdateLoopManager.getInstance().handle(this);
    }
}
