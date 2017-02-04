package com.ede.standyourground.app.service;

import android.util.Log;
import android.view.animation.LinearInterpolator;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Eddie on 2/2/2017.
 */

public class MathUtils {

    private static final String TAG = MathUtils.class.getName();
    private static final LinearInterpolator linearInterpolator = new LinearInterpolator();

    public static float bearing(LatLng to, LatLng from) {
        double dLon = from.longitude - to.longitude;
        double y = Math.sin(dLon) * Math.cos(from.latitude);
        double x = Math.cos(to.latitude) * Math.sin(from.latitude) - Math.sin(to.latitude) * Math.cos(from.latitude) * Math.cos(dLon);
        double bearing = Math.toDegrees(Math.atan2(y, x));
        bearing = (360 - ((bearing + 360) % 360));
        Log.i(TAG, "bearing calculated to be " + bearing);
        return (float) bearing;
    }


    public static LatLng midpoint(LatLng l1, LatLng l2) {
        double lat1 = l1.latitude;
        double lat2 = l2.latitude;
        double lon1 = l1.longitude;
        double lon2 = l2.longitude;

        double dLon = Math.toRadians(lon2 - lon1);

        //convert to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        lon1 = Math.toRadians(lon1);

        double Bx = Math.cos(lat2) * Math.cos(dLon);
        double By = Math.cos(lat2) * Math.sin(dLon);

        return new LatLng(Math.toDegrees(Math.atan2(Math.sin(lat1) + Math.sin(lat2), Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By))),
                          Math.toDegrees(lon1 + Math.atan2(By, Math.cos(lat1) + Bx)));
    }
}
