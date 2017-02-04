package com.ede.standyourground.game.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Eddie on 2/3/2017.
 */

public class Unit{
    private AtomicReference<LatLng> position;
    public final UUID id = UUID.randomUUID();

    public LatLng getPosition() {
        return position.get();
    }

    public void setPosition(LatLng position) {
        this.position.set(position);
    }

    public UUID getId() {
        return id;
    }
}
