package com.ede.standyourground.game.model.api;


public interface Mortal {
    void registerDeathListener(DeathListener deathListener);
    void onDeath();
}
