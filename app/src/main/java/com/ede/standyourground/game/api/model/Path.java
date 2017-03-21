package com.ede.standyourground.game.api.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Path {
    private List<LatLng> points = new ArrayList<>();
    private List<Integer> distances = new ArrayList<>();

    public Path(List<LatLng> points, List<Integer> distances) {
        this.points = points;
        this.distances = distances;
    }

    public List<Integer> getDistances() {
        return distances;
    }

    public List<LatLng> getPoints() {
        return points;
    }
}
