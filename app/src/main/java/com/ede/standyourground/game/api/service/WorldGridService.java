package com.ede.standyourground.game.api.service;

import com.ede.standyourground.game.api.model.Coordinate;
import com.ede.standyourground.game.api.model.Unit;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 *
 */

public interface WorldGridService {
    Coordinate calculateCoordinatePosition(LatLng position);
    void removeUnitAtCoordinate(Coordinate coordinate, Unit unit);
    void addUnitAtCoordinate(Coordinate coordinate, Unit unit);
    List<Unit> retrieveUnitsAtCoordinate(Coordinate coordinate);
}
