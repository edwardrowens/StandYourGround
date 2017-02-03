package com.ede.standyourground.service;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Eddie on 2/2/2017.
 */

public class MathUtils {

    public static float bearing(LatLng l1, LatLng l2) {
        double dLon = l2.longitude - l1.longitude;
        double y = Math.sin(dLon) * Math.cos(l2.latitude);
        double x = Math.cos(l1.latitude) * Math.sin(l2.latitude) - Math.sin(l1.latitude) * Math.cos(l2.latitude) * Math.cos(dLon);
        double bearing = Math.toDegrees(Math.atan2(y, x));
        bearing = (360 - ((bearing + 360) % 360));
        return (float) bearing;
    }
}
