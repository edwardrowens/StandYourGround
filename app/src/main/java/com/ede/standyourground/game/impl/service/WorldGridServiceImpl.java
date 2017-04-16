package com.ede.standyourground.game.impl.service;

import com.ede.standyourground.game.api.model.Coordinate;
import com.ede.standyourground.game.api.model.Unit;
import com.ede.standyourground.game.api.model.WorldGrid;
import com.ede.standyourground.game.api.service.GameService;
import com.ede.standyourground.game.api.service.WorldGridService;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;

/**
 *
 */

public class WorldGridServiceImpl implements WorldGridService {

    private final Lazy<GameService> gameService;

    @Inject
    public WorldGridServiceImpl(Lazy<GameService> gameService) {
        this.gameService = gameService;
    }

    @Override
    public Coordinate calculateCoordinatePosition(LatLng position) {
        LatLng referencePosition = gameService.get().getWorldGrid().getReferencePosition();
        double heading = SphericalUtil.computeHeading(referencePosition, position);
        double headingAbsolute = Math.abs(heading);
        double theta = headingAbsolute > 90 ? headingAbsolute - 90 : 90 - headingAbsolute;
        double distance = SphericalUtil.computeDistanceBetween(referencePosition, position);
        double deltaY = distance * Math.sin(Math.toRadians(theta));
        double deltaX = distance * Math.cos(Math.toRadians(theta));

        int x = (int)deltaX / WorldGrid.CELL_LENGTH;
        int y = (int)deltaY / WorldGrid.CELL_LENGTH;

        x = heading > 0 ? x : -x;
        y = headingAbsolute < 90 ? y : -y;

        return new Coordinate(x, y);
    }

    @Override
    public void removeUnitAtCoordinate(Coordinate coordinate, Unit unit) {
        WorldGrid worldGrid = gameService.get().getWorldGrid();
        List<Unit> unitsAtCoordinate = worldGrid.getGrid().get(coordinate);
        if (unitsAtCoordinate != null && unitsAtCoordinate.contains(unit)) {
            unitsAtCoordinate.remove(unit);
        }
    }

    @Override
    public void addUnitAtCoordinate(Coordinate coordinate, Unit unit) {
        WorldGrid worldGrid = gameService.get().getWorldGrid();
        List<Unit> unitsAtCoordinate = worldGrid.getGrid().get(coordinate);
        if (unitsAtCoordinate != null) {
            unitsAtCoordinate.add(unit);
        } else {
            worldGrid.getGrid().put(coordinate, new LinkedList<>(Collections.singletonList(unit)));
        }
    }

    @Override
    public List<Unit> retrieveUnitsAtCoordinate(Coordinate coordinate) {
        WorldGrid worldGrid = gameService.get().getWorldGrid();
        return worldGrid.getGrid().get(coordinate);
    }
}
