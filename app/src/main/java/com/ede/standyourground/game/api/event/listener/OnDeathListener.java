package com.ede.standyourground.game.api.event.listener;


import com.ede.standyourground.game.api.model.Unit;

public interface OnDeathListener {
    void onDeath(Unit mortal, Unit killer);
}
