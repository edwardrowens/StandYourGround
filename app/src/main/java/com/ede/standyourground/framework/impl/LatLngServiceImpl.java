package com.ede.standyourground.framework.impl;

import com.ede.standyourground.framework.api.LatLngService;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.List;

import javax.inject.Inject;


public class LatLngServiceImpl implements LatLngService {

    @Inject
    public LatLngServiceImpl() {

    }

    @Override
    public double bearing(LatLng to, LatLng from) {
        return SphericalUtil.computeHeading(to, from);
    }

    @Override
    public LatLng midpoint(LatLng l1, LatLng l2) {
        return SphericalUtil.interpolate(l1, l2, .5);
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
        double d = SphericalUtil.computeDistanceBetween(position1, position2);
        return d <= distance;
    }

    @Override
    public double calculateDistance(LatLng p1, LatLng p2) {
        return SphericalUtil.computeDistanceBetween(p1, p2);
    }
}
