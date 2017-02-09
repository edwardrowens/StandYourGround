package com.ede.standyourground.game.model;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

/**
 * Created by Eddie on 2/4/2017.
 */

public class FootSoldier extends MovableUnit {
    public FootSoldier(double mph, Polyline polyline, LatLng position, Circle circle) {
        super(mph, polyline, position, circle);
    }
}
