package com.ede.standyourground.framework;

import com.ede.standyourground.game.model.Unit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eddie on 2/4/2017.
 */

public class UpdateLoopTask {

    private List<Unit> updatedUnits = new ArrayList<>();
    public List<Unit> getUpdatedUnits() {
        return updatedUnits;
    }

    public void setUpdatedUnits(List<Unit> updatedUnits) {
        this.updatedUnits = updatedUnits;
    }


    public void send() {
        UpdateLoopManager.getInstance().handle(this);
    }
}
