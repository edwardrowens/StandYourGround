package com.ede.standyourground.game.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Eddie on 2/4/2017.
 */

public class FootSoldier extends MovableUnit {
    public FootSoldier(int speed, Path path, LatLng position) {
        super(speed, path, position);
    }
}
