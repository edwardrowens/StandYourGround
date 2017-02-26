package com.ede.standyourground.framework.api;

import com.ede.standyourground.app.model.Routes;
import com.ede.standyourground.game.model.Path;
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
