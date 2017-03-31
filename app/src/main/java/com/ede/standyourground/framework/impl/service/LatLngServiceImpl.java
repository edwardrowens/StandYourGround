package com.ede.standyourground.framework.impl.service;

import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.framework.api.service.LatLngService;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.SphericalUtil;

import java.util.List;

import javax.inject.Inject;


public class LatLngServiceImpl implements LatLngService {

    private static final Logger logger = new Logger(LatLngServiceImpl.class);

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
    public LatLngBounds createBounds(LatLng origin, LatLng pointOfInterest) {
        logger.i("Generating bounds for origin <%s> and POI <%s>", origin, pointOfInterest);
        double bearing = SphericalUtil.computeHeading(origin, pointOfInterest);
        double distance = SphericalUtil.computeDistanceBetween(origin, pointOfInterest);

        double northEastBearing = bearing + 45 >= 180 ? bearing + 45 - 180 - 180 : bearing + 45;
        double southWestBearing = bearing - 90 < -180 ? bearing - 90 + 180 + 180 : bearing - 90;

        LatLng northEastPoint = SphericalUtil.computeOffset(origin, distance, northEastBearing);
        LatLng southWestPoint = SphericalUtil.computeOffset(origin, distance, southWestBearing);

        logger.i("North eastern bound <%s>. South western bound <%s>", northEastPoint, southWestPoint);

        return LatLngBounds.builder().include(northEastPoint).include(southWestPoint).include(origin).include(pointOfInterest).build();
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
