package com.ede.standyourground.app.to;

import java.util.UUID;

/**
 * Created by Eddie on 2/9/2017.
 */

public class FindMatchRequestTO {
    private double lat;
    private double lng;
    private int radius;
    private UUID id;
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

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
