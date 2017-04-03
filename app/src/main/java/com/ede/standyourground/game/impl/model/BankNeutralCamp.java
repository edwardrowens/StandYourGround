package com.ede.standyourground.game.impl.model;

import com.ede.standyourground.game.api.model.Hostility;
import com.ede.standyourground.game.api.model.NeutralCamp;
import com.ede.standyourground.game.api.model.Units;
import com.google.android.gms.maps.model.LatLng;

/**
 *
 */

public class BankNeutralCamp extends NeutralCamp {
    private static final int HEALTH = 100;
    private static final int INCOME_PROVIDED = 5;

    public BankNeutralCamp(LatLng startingPosition, String name, String photoReference, Hostility hostility) {
        super(startingPosition, Units.BANK_NEUTRAL_CAMP, name, photoReference, hostility);
    }

    @Override
    public int getMaxHealth() {
        return HEALTH;
    }

    @Override
    public double getVisionRadius() {
        return 0;
    }

    public int getProvidedIncome() {
        return INCOME_PROVIDED;
    }
}