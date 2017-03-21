package com.ede.standyourground.framework.api.service;


import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public interface LatLngService {

    double bearing(LatLng to, LatLng from);

    LatLng midpoint(LatLng l1, LatLng l2);

    int sumTo(List<Integer> list, int index);

    boolean withinDistance(LatLng position1, LatLng position2, double distance);

    /**
     * Calculates the linear distance between two points on a map in meters.
     *
     * @param p1
     * @param p2
     * @return
     */
    double calculateDistance(LatLng p1, LatLng p2);
}
