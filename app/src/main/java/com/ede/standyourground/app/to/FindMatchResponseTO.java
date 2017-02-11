package com.ede.standyourground.app.to;

/**
 * Created by Eddie on 2/9/2017.
 */

public class FindMatchResponseTO {
    private double lat;
    private double lng;
    private String ip;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
