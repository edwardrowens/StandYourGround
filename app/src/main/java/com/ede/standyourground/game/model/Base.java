package com.ede.standyourground.game.model;


import com.google.android.gms.maps.model.LatLng;

public class Base extends Unit {
    public Base(LatLng position, boolean isEnemy) {
        super(position, isEnemy);
    }

    @Override
    public void onRender() {

    }
}
