package com.ede.standyourground.game.api.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public class WorldGrid {

    public static final int CELL_LENGTH = 500;

    private final Map<Coordinate, List<Unit>> grid = new ConcurrentHashMap<>();
    private final LatLng referencePosition;

    public WorldGrid(LatLng referencePosition) {
        this.referencePosition = referencePosition;
    }

    public LatLng getReferencePosition() {
        return referencePosition;
    }

    public Map<Coordinate, List<Unit>> getGrid() {
        return grid;
    }
}
