package com.ede.standyourground.game.impl.service;

import com.ede.standyourground.game.api.model.Cell;
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
    public Cell calculateCellPosition(LatLng position) {
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

        return new Cell(x, y);
    }

    @Override
    public void removeUnitAtCell(Cell cell, Unit unit) {
        WorldGrid worldGrid = gameService.get().getWorldGrid();
        List<Unit> unitsAtCoordinate = worldGrid.getGrid().get(cell);
        if (unitsAtCoordinate != null && unitsAtCoordinate.contains(unit)) {
            unitsAtCoordinate.remove(unit);
        }
    }

    @Override
    public void addUnitAtCell(Cell cell, Unit unit) {
        WorldGrid worldGrid = gameService.get().getWorldGrid();
        List<Unit> unitsAtCoordinate = worldGrid.getGrid().get(cell);
        if (unitsAtCoordinate != null) {
            unitsAtCoordinate.add(unit);
        } else {
            worldGrid.getGrid().put(cell, new LinkedList<>(Collections.singletonList(unit)));
        }
    }

    @Override
    public List<Unit> retrieveUnitsAtCell(Cell cell) {
        List<Unit> toReturn = gameService.get().getWorldGrid().getGrid().get(cell);
        return toReturn == null ? new LinkedList<Unit>() : toReturn;
    }

    @Override
    public List<Unit> retrieveUnitsInCellRange(Cell cell, double distance) {
        int range = (int) Math.ceil(distance / WorldGrid.CELL_LENGTH);
        List<Unit> unitsInRange = new LinkedList<>();
        unitsInRange.addAll(retrieveUnitsAtCell(cell));

        for (int x = 1; x <= range; ++x) {
            for (int y = 1; y <= range; ++y) {
                // Q1
                unitsInRange.addAll(retrieveUnitsAtCell(new Cell(cell.getX() + x, cell.getY() + y)));
                // Q2
                unitsInRange.addAll(retrieveUnitsAtCell(new Cell(cell.getX() - x, cell.getY() + y)));
                // Q3
                unitsInRange.addAll(retrieveUnitsAtCell(new Cell(cell.getX() - x, cell.getY() - y)));
                // Q4
                unitsInRange.addAll(retrieveUnitsAtCell(new Cell(cell.getX() + x, cell.getY() - y)));
            }
            // (+) y-axis
            unitsInRange.addAll(retrieveUnitsAtCell(new Cell(cell.getX(), cell.getY() + x)));
            // (-) y-axis
            unitsInRange.addAll(retrieveUnitsAtCell(new Cell(cell.getX(), cell.getY() - x)));
            // (+) x-axis
            unitsInRange.addAll(retrieveUnitsAtCell(new Cell(cell.getX() + x, cell.getY())));
            // (-) x-axis
            unitsInRange.addAll(retrieveUnitsAtCell(new Cell(cell.getX() - x, cell.getY())));
        }

        return unitsInRange;
    }
}
