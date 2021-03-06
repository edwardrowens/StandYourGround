package com.ede.standyourground.framework.impl.service;

import android.location.Location;

import com.ede.standyourground.framework.api.service.RouteService;
import com.ede.standyourground.game.api.model.Path;
import com.ede.standyourground.networking.api.model.Leg;
import com.ede.standyourground.networking.api.model.Route;
import com.ede.standyourground.networking.api.model.Routes;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RouteServiceImpl implements RouteService {

    private static final double FEET_PER_VALUE = 3.28084;
    private static final int FEET_PER_MILE = 5280;

    @Inject
    RouteServiceImpl() {

    }

    @Override
    public int getTotalDistance(Routes routes) {
        int distance = 0;
        for (Route route : routes.getRoutes()) {
            for (Leg leg : route.getLegs()) {
                distance += leg.getDistance().getValue();
            }
        }
        return distance;
    }

    @Override
    public List<Integer> getDistanceOfSteps(List<LatLng> points, LatLng currentPosition) {
        List<Integer> distances = new ArrayList<>();
        if (points.size() == 0)
            return distances;

        float[] results = new float[1];

        Location.distanceBetween(currentPosition.latitude,currentPosition.longitude, points.get(0).latitude, points.get(0).longitude, results);
        distances.add(Math.round(results[0]));

        for (int i = 0; i < points.size() - 1; ++i) {
            Location.distanceBetween(points.get(i).latitude,points.get(i).longitude, points.get(i+1).latitude, points.get(i+1).longitude, results);
            distances.add(Math.round(results[0]));
        }
        return distances;
    }

    @Override
    public double valueToMiles(int value) {
        return value * FEET_PER_VALUE / FEET_PER_MILE;
    }

    @Override
    public int milesToValue(double miles) {
        return (int)Math.round(miles * FEET_PER_MILE / FEET_PER_VALUE);
    }

    @Override
    public int timeToDestination(double distance, double mph) {
        return (int)Math.round(distance/mph * 60 * 60 * 1000);
    }

    @Override
    public int calculateTotalDistance(Path path) {
        int total = 0;
        for (int d : path.getDistances())
            total += d;
        return total;
    }
}
