package com.ede.standyourground;

import com.ede.standyourground.framework.Lazy;
import com.ede.standyourground.game.api.model.Cell;
import com.ede.standyourground.game.api.model.Unit;
import com.ede.standyourground.game.api.model.WorldGrid;
import com.ede.standyourground.game.api.service.GameService;
import com.ede.standyourground.game.impl.service.WorldGridServiceImpl;
import com.google.android.gms.maps.model.LatLng;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TestWorldGridServiceImpl {

    private static final LatLng REFERENCE = new LatLng(34.459509, -116.301739);

    @Mock(answer = Answers.RETURNS_DEEP_STUBS) private GameService gameService;

    private WorldGridServiceImpl worldGridServiceImpl;

    @Before
    public void setup() {
        when(gameService.getWorldGrid().getReferencePosition()).thenReturn(REFERENCE);

        worldGridServiceImpl = new WorldGridServiceImpl(Lazy.of(gameService));
    }

    @Test
    public void testCalculateCoordinatePositionQ1() {
        // Given
        LatLng position = new LatLng(34.495827, -116.235776);

        // When
        Cell cell = worldGridServiceImpl.calculateCellPosition(position);

        // Then
        Assert.assertEquals("Incorrect x was calculated", 12, cell.getX());
        Assert.assertEquals("Incorrect y was calculated", 8, cell.getY());
    }

    @Test
    public void testCalculateCoordinatePositionQ2() {
        // Given
        LatLng position = new LatLng(34.519701, -116.382637);

        // When
        Cell cell = worldGridServiceImpl.calculateCellPosition(position);

        // Then
        Assert.assertEquals("Incorrect x was calculated", -14, cell.getX());
        Assert.assertEquals("Incorrect y was calculated", 13, cell.getY());
    }

    @Test
    public void testCalculateCellPositionQ3() {
        // Given
        LatLng position = new LatLng(34.449813, -116.316792);

        // When
        Cell cell = worldGridServiceImpl.calculateCellPosition(position);

        // Then
        Assert.assertEquals("Incorrect x was calculated", -2, cell.getX());
        Assert.assertEquals("Incorrect y was calculated", -2, cell.getY());
    }

    @Test
    public void testCalculateCoordinatePositionQ4() {
        // Given
        LatLng position = new LatLng(34.448908, -116.286565);

        // When
        Cell cell = worldGridServiceImpl.calculateCellPosition(position);

        // Then
        Assert.assertEquals("Incorrect x was calculated", 2, cell.getX());
        Assert.assertEquals("Incorrect y was calculated", -2, cell.getY());
    }

    @Test
    public void testRetrieveUnitsInCellRange() {
        // Given
        Cell cell1 = new Cell(0, 0);
        Cell cell2 = new Cell(1, 4);
        Cell cell3 = new Cell(-1, 2);
        Cell cell4 = new Cell(-2, -3);
        Cell cell5 = new Cell(10, 10);
        Cell cell6 = new Cell(0, 2);
        Cell cell7 = new Cell(0, -1);
        Cell cell8 = new Cell(1, 0);
        Cell cell9 = new Cell(-3, 0);
        Cell cell10 = new Cell(2, 2);
        Cell cell11 = new Cell(2, -3);
        Cell cell12 = new Cell(2, -4);
        Cell cell13 = new Cell(2, -2);

        Unit unit1 = mock(Unit.class);
        Unit unit2 = mock(Unit.class);
        Unit unit3 = mock(Unit.class);
        Unit unit4 = mock(Unit.class);
        Unit unit5 = mock(Unit.class);
        Unit unit6 = mock(Unit.class);
        Unit unit7 = mock(Unit.class);
        Unit unit8 = mock(Unit.class);
        Unit unit9 = mock(Unit.class);
        Unit unit10 = mock(Unit.class);
        Unit unit11 = mock(Unit.class);
        Unit unit12 = mock(Unit.class);
        Unit unit13 = mock(Unit.class);

        WorldGrid worldGrid = new WorldGrid(new LatLng(0, 0));
        worldGrid.getGrid().put(cell1, Collections.singleton(unit1));
        worldGrid.getGrid().put(cell2, Collections.singleton(unit2));
        worldGrid.getGrid().put(cell3, Collections.singleton(unit3));
        worldGrid.getGrid().put(cell4, Collections.singleton(unit4));
        worldGrid.getGrid().put(cell5, Collections.singleton(unit5));
        worldGrid.getGrid().put(cell6, Collections.singleton(unit6));
        worldGrid.getGrid().put(cell7, Collections.singleton(unit7));
        worldGrid.getGrid().put(cell8, Collections.singleton(unit8));
        worldGrid.getGrid().put(cell9, Collections.singleton(unit9));
        worldGrid.getGrid().put(cell10, Collections.singleton(unit10));
        worldGrid.getGrid().put(cell11, Collections.singleton(unit11));
        worldGrid.getGrid().put(cell12, Collections.singleton(unit12));
        worldGrid.getGrid().put(cell13, Collections.singleton(unit13));

        when(gameService.getWorldGrid()).thenReturn(worldGrid);

        // When
        List<Unit> result = worldGridServiceImpl.retrieveUnitsInCellRange(cell1, 1500);

        // Then
        Assert.assertEquals("Incorrect list size", 10, result.size());
        Assert.assertTrue("Unit 1 should be present", result.contains(unit1));
        Assert.assertTrue("Unit 3 should be present", result.contains(unit3));
        Assert.assertTrue("Unit 4 should be present", result.contains(unit4));
        Assert.assertTrue("Unit 6 should be present", result.contains(unit6));
        Assert.assertTrue("Unit 7 should be present", result.contains(unit7));
        Assert.assertTrue("Unit 8 should be present", result.contains(unit8));
        Assert.assertTrue("Unit 9 should be present", result.contains(unit9));
        Assert.assertTrue("Unit 10 should be present", result.contains(unit10));
        Assert.assertTrue("Unit 11 should be present", result.contains(unit11));
        Assert.assertTrue("Unit 13 should be present", result.contains(unit13));
    }

    @Test
    public void testRetrieveUnitsInCellRange1Meter() {
        // Given
        Cell cell1 = new Cell(0, 0);
        Cell cell2 = new Cell(1, 4);
        Cell cell3 = new Cell(-1, 2);
        Cell cell4 = new Cell(-2, -3);
        Cell cell5 = new Cell(10, 10);
        Cell cell6 = new Cell(0, 2);
        Cell cell7 = new Cell(0, -1);
        Cell cell8 = new Cell(1, 0);
        Cell cell9 = new Cell(-3, 0);
        Cell cell10 = new Cell(2, 2);
        Cell cell11 = new Cell(2, -3);
        Cell cell12 = new Cell(2, -4);

        Unit unit1 = mock(Unit.class);
        Unit unit2 = mock(Unit.class);
        Unit unit3 = mock(Unit.class);
        Unit unit4 = mock(Unit.class);
        Unit unit5 = mock(Unit.class);
        Unit unit6 = mock(Unit.class);
        Unit unit7 = mock(Unit.class);
        Unit unit8 = mock(Unit.class);
        Unit unit9 = mock(Unit.class);
        Unit unit10 = mock(Unit.class);
        Unit unit11 = mock(Unit.class);
        Unit unit12 = mock(Unit.class);

        WorldGrid worldGrid = new WorldGrid(new LatLng(0, 0));
        worldGrid.getGrid().put(cell1, Collections.singleton(unit1));
        worldGrid.getGrid().put(cell2, Collections.singleton(unit2));
        worldGrid.getGrid().put(cell3, Collections.singleton(unit3));
        worldGrid.getGrid().put(cell4, Collections.singleton(unit4));
        worldGrid.getGrid().put(cell5, Collections.singleton(unit5));
        worldGrid.getGrid().put(cell6, Collections.singleton(unit6));
        worldGrid.getGrid().put(cell7, Collections.singleton(unit7));
        worldGrid.getGrid().put(cell8, Collections.singleton(unit8));
        worldGrid.getGrid().put(cell9, Collections.singleton(unit9));
        worldGrid.getGrid().put(cell10, Collections.singleton(unit10));
        worldGrid.getGrid().put(cell11, Collections.singleton(unit11));
        worldGrid.getGrid().put(cell12, Collections.singleton(unit12));

        when(gameService.getWorldGrid()).thenReturn(worldGrid);

        // When
        List<Unit> result = worldGridServiceImpl.retrieveUnitsInCellRange(cell1, 1);

        // Then
        Assert.assertEquals("Incorrect list size", 3, result.size());
        Assert.assertTrue("Unit 1 should be present", result.contains(unit1));
        Assert.assertTrue("Unit 7 should be present", result.contains(unit7));
        Assert.assertTrue("Unit 8 should be present", result.contains(unit8));
    }

    @Test
    public void testRetrieveUnitsInCellRange0Meter() {
        // Given
        Cell cell1 = new Cell(0, 0);
        Cell cell2 = new Cell(1, 4);
        Cell cell3 = new Cell(-1, 2);
        Cell cell4 = new Cell(-2, -3);
        Cell cell5 = new Cell(10, 10);
        Cell cell6 = new Cell(0, 2);
        Cell cell7 = new Cell(0, -1);
        Cell cell8 = new Cell(1, 0);
        Cell cell9 = new Cell(-3, 0);
        Cell cell10 = new Cell(2, 2);
        Cell cell11 = new Cell(2, -3);
        Cell cell12 = new Cell(2, -4);

        Unit unit1 = mock(Unit.class);
        Unit unit2 = mock(Unit.class);
        Unit unit3 = mock(Unit.class);
        Unit unit4 = mock(Unit.class);
        Unit unit5 = mock(Unit.class);
        Unit unit6 = mock(Unit.class);
        Unit unit7 = mock(Unit.class);
        Unit unit8 = mock(Unit.class);
        Unit unit9 = mock(Unit.class);
        Unit unit10 = mock(Unit.class);
        Unit unit11 = mock(Unit.class);
        Unit unit12 = mock(Unit.class);

        WorldGrid worldGrid = new WorldGrid(new LatLng(0, 0));
        worldGrid.getGrid().put(cell1, Collections.singleton(unit1));
        worldGrid.getGrid().put(cell2, Collections.singleton(unit2));
        worldGrid.getGrid().put(cell3, Collections.singleton(unit3));
        worldGrid.getGrid().put(cell4, Collections.singleton(unit4));
        worldGrid.getGrid().put(cell5, Collections.singleton(unit5));
        worldGrid.getGrid().put(cell6, Collections.singleton(unit6));
        worldGrid.getGrid().put(cell7, Collections.singleton(unit7));
        worldGrid.getGrid().put(cell8, Collections.singleton(unit8));
        worldGrid.getGrid().put(cell9, Collections.singleton(unit9));
        worldGrid.getGrid().put(cell10, Collections.singleton(unit10));
        worldGrid.getGrid().put(cell11, Collections.singleton(unit11));
        worldGrid.getGrid().put(cell12, Collections.singleton(unit12));

        when(gameService.getWorldGrid()).thenReturn(worldGrid);

        // When
        List<Unit> result = worldGridServiceImpl.retrieveUnitsInCellRange(cell1, 0);

        // Then
        Assert.assertEquals("Incorrect list size", 1, result.size());
        Assert.assertTrue("Should only add original cell", result.contains(unit1));
    }
}