package com.ede.standyourground.game.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Eddie on 2/4/2017.
 */

public class FootSoldier extends MovableUnit {
    public FootSoldier(int speed, List<LatLng> path, LatLng position) {
        super(speed, path, position);
    }
}
