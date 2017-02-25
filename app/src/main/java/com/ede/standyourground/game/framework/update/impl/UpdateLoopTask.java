package com.ede.standyourground.game.framework.update.impl;

import com.ede.standyourground.game.model.Unit;

import java.util.ArrayList;
import java.util.List;


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
