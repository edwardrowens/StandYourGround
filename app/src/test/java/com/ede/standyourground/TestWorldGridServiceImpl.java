package com.ede.standyourground;

import com.ede.standyourground.framework.Lazy;
import com.ede.standyourground.game.api.model.Coordinate;
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
        Coordinate coordinate = worldGridServiceImpl.calculateCoordinatePosition(position);

        // Then
        Assert.assertEquals("Incorrect x was calculated", 12, coordinate.getX());
        Assert.assertEquals("Incorrect y was calculated", 8, coordinate.getY());
    }

    @Test
    public void testCalculateCoordinatePositionQ2() {
        // Given
        LatLng position = new LatLng(34.519701, -116.382637);

        // When
        Coordinate coordinate = worldGridServiceImpl.calculateCoordinatePosition(position);

        // Then
        Assert.assertEquals("Incorrect x was calculated", -14, coordinate.getX());
        Assert.assertEquals("Incorrect y was calculated", 13, coordinate.getY());
    }

    @Test
    public void testCalculateCoordinatePositionQ3() {
        // Given
        LatLng position = new LatLng(34.449813, -116.316792);

        // When
        Coordinate coordinate = worldGridServiceImpl.calculateCoordinatePosition(position);

        // Then
        Assert.assertEquals("Incorrect x was calculated", -2, coordinate.getX());
        Assert.assertEquals("Incorrect y was calculated", -2, coordinate.getY());
    }

    @Test
    public void testCalculateCoordinatePositionQ4() {
        // Given
        LatLng position = new LatLng(34.448908, -116.286565);

        // When
        Coordinate coordinate = worldGridServiceImpl.calculateCoordinatePosition(position);

        // Then
        Assert.assertEquals("Incorrect x was calculated", 2, coordinate.getX());
        Assert.assertEquals("Incorrect y was calculated", -2, coordinate.getY());
    }
}