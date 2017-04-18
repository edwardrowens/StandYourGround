package com.ede.standyourground.game.api.model;

import com.google.android.gms.maps.model.LatLng;

/**
 *
 */

public abstract class NeutralCamp extends Unit {

    private final String name;
    private final String photoReference;

    public NeutralCamp(LatLng startingPosition, Units type, String name, String photoReference, Hostility hostility, Cell cell) {
        super(startingPosition, type, hostility, cell);
        this.name = name;
        this.photoReference = photoReference;
    }

    @Override
    protected void onUnitDeath() {

    }

    public String getName() {
        return name;
    }

    public String getPhotoReference() {
        return photoReference;
    }
}
