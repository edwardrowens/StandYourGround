package com.ede.standyourground.game.api.model;

/**
 *
 */

public interface Healable {
    Hostility getHostility();
    void incrementHealth(int value);
    int getMaxHealth();
    int getHealth();
}
