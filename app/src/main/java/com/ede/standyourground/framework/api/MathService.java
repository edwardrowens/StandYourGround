package com.ede.standyourground.framework.api;


import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public interface MathService {

    float bearing(LatLng to, LatLng from);

    LatLng midpoint(LatLng l1, LatLng l2);

    int sumTo(List<Integer> list, int index);

    boolean withinDistance(LatLng position1, LatLng position2, double distance);
}
