package com.ede.standyourground.game.impl.model;

import com.ede.standyourground.game.api.model.Cell;
import com.ede.standyourground.game.api.model.Hostility;
import com.ede.standyourground.game.api.model.NeutralCamp;
import com.ede.standyourground.game.api.model.UnitType;
import com.google.android.gms.maps.model.LatLng;

/**
 *
 */

public class MedicNeutralCamp extends NeutralCamp {
    private static final int HEALTH = 100;

    public MedicNeutralCamp(LatLng startingPosition, String name, String photoReference, Hostility hostility, Cell cell) {
        super(startingPosition, UnitType.MEDIC_NEUTRAL_CAMP, name, photoReference, hostility, cell);
    }

    @Override
    public int getMaxHealth() {
        return HEALTH;
    }

    @Override
    public double getVisionRadius() {
        return 0;
    }
}
