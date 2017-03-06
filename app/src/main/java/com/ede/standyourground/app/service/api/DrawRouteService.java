package com.ede.standyourground.app.service.api;


import com.ede.standyourground.game.model.Units;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

/**
 * This service can only be used on the main thread as it manipulates google map
 * assets.
 */
public interface DrawRouteService {
    void drawRoutesForUnit(Units toCreate, List<Marker> markers, LatLng playerLocation, LatLng opponentLocation);
    void drawRoutesForEnemyUnit(Units toCreate, List<Marker> markers, LatLng playerLocation, LatLng opponentLocation);
}
