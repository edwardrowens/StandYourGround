package com.ede.standyourground;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        LatLng me = new LatLng(34.154983, -118.247269);
//        LatLng thaiMeUp = new LatLng(34.158010, -118.263380);
        LatLng thaiMeUp = new LatLng(34.140432, -118.244185);
        double bearing = SphericalUtil.computeHeading(me, thaiMeUp);
        double distance = SphericalUtil.computeDistanceBetween(me, thaiMeUp);

        System.out.println(String.format("bearing: %.5f", bearing));
        System.out.println(String.format("distance: %.5f", distance));

        double northEastBearing = bearing + 45 >= 180 ? bearing + 45 - 180 - 180 : bearing + 45;
        System.out.println(String.format("northEastBearing: %.5f", northEastBearing));
        double southWestBearing = bearing - 90 < -180 ? bearing - 90 + 180 + 180 : bearing - 90;
        System.out.println(String.format("southWestBearing: %.5f", southWestBearing));

        System.out.println(String.format("northeast: %s", SphericalUtil.computeOffset(me, distance, northEastBearing)));
        System.out.println(String.format("southwest: %s", SphericalUtil.computeOffset(me, distance, southWestBearing)));
    }
}