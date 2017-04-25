package com.ede.standyourground.framework.api.service;


import com.ede.standyourground.game.api.model.UnitType;
import com.ede.standyourground.networking.api.model.Routes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

import retrofit2.Callback;

public interface DrawRouteService {
    void createRoutesForUnit(LatLng start, LatLng end, List<LatLng> intermediaryPoints, Callback<Routes> callback);
    void createRoutesForEnemyUnit(UnitType toCreate, List<Marker> markers, LatLng playerLocation, LatLng opponentLocation);
}
