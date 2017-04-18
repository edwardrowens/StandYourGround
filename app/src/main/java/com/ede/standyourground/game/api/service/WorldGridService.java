package com.ede.standyourground.game.api.service;

import com.ede.standyourground.game.api.model.Cell;
import com.ede.standyourground.game.api.model.Unit;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 *
 */

public interface WorldGridService {
    Cell calculateCellPosition(LatLng position);
    void moveUnitCell(Cell fromCell, Cell toCell, Unit unit);
    void removeUnitAtCell(Cell cell, Unit unit);
    void addUnitAtCell(Cell cell, Unit unit);
    List<Unit> retrieveUnitsAtCell(Cell cell);
    List<Unit> retrieveUnitsInCellRange(Cell cell, double distance);
}
