package com.ede.standyourground.game.model.api;


import com.ede.standyourground.game.model.Unit;

public interface DeathListener {
    void onDeath(Unit mortal);
}
