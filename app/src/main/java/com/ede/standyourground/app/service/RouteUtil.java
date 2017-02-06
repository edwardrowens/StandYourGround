package com.ede.standyourground.app.service;

import com.ede.standyourground.app.model.Leg;
import com.ede.standyourground.app.model.Route;
import com.ede.standyourground.app.model.Routes;
import com.ede.standyourground.app.model.Step;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eddie on 2/6/2017.
 */

public class RouteUtil {
    public static int getTotalDistance(Routes routes) {
        int distance = 0;
        for (Route route : routes.getRoutes()) {
            for (Leg leg : route.getLegs()) {
                distance += leg.getDistance().getValue();
            }
        }
        return distance;
    }

    public static List<Integer> getDistanceOfSteps(Routes routes) {
        List<Integer> proportion = new ArrayList<>();
        for (Route route : routes.getRoutes()) {
            for (Leg leg : route.getLegs()) {
                for (Step step : leg.getSteps()) {
                    proportion.add(step.getDistance().getValue());
                }
            }
        }
        return proportion;
    }
}
