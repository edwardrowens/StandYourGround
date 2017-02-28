package com.ede.standyourground.framework.impl;

import com.ede.standyourground.framework.api.LatLngService;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import javax.inject.Inject;


public class LatLngServiceImpl implements LatLngService {

    private static final int EARTH_RADIUS = 3959;

    @Inject
    public LatLngServiceImpl() {

    }

    @Override
    public float bearing(LatLng to, LatLng from) {
        double dLon = from.longitude - to.longitude;
        double y = Math.sin(dLon) * Math.cos(from.latitude);
        double x = Math.cos(to.latitude) * Math.sin(from.latitude) - Math.sin(to.latitude) * Math.cos(from.latitude) * Math.cos(dLon);
        double bearing = Math.toDegrees(Math.atan2(y, x));
        bearing = (360 - ((bearing + 360) % 360));
        return (float) bearing;
    }

    @Override
    public LatLng midpoint(LatLng l1, LatLng l2) {
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

    @Override
    public int sumTo(List<Integer> list, int index) {
        int sum = 0;
        for (int i = 0; i < index; ++i)
            sum += list.get(i);
        return sum;
    }

    @Override
    public boolean withinDistance(LatLng position1, LatLng position2, double distance) {
        double dLat = deg2Rad(position2.latitude - position1.latitude);
        double dLon = deg2Rad(position2.longitude - position1.longitude);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(deg2Rad(position1.latitude)) * Math.cos(deg2Rad(position2.latitude)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = EARTH_RADIUS * c;
        return d <= distance;
    }

    @Override
    public double calculateDistance(LatLng p1, LatLng p2) {
        double dLat = deg2Rad(p2.latitude - p1.latitude);
        double dLon = deg2Rad(p2.longitude - p1.longitude);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(deg2Rad(p1.latitude)) * Math.cos(deg2Rad(p2.latitude)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }

    private double deg2Rad(double degrees) {
        return degrees * (Math.PI / 180);
    }
}
