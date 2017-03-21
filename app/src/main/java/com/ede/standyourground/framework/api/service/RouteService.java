package com.ede.standyourground.framework.api.service;

import com.ede.standyourground.game.api.model.Path;
import com.ede.standyourground.networking.api.model.Routes;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public interface RouteService {
    int calculateTotalDistance(Path path);

    int timeToDestination(double distance, double mph);

    int milesToValue(double miles);

    double valueToMiles(int value);

    List<Integer> getDistanceOfSteps(List<LatLng> points, LatLng currentPosition);

    int getTotalDistance(Routes routes);
}
