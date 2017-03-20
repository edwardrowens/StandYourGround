package com.ede.standyourground.game.framework.management.api;

import com.ede.standyourground.game.model.Unit;
import com.ede.standyourground.game.model.Units;
import com.ede.standyourground.game.model.api.PositionChangeObserver;
import com.ede.standyourground.game.model.api.VisibilityChangeObserver;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.UUID;

/**
 *
 */
public interface UnitService extends DeathObserver, HealthChangeObserver, GameEndObserver, UnitCreatedObserver, PositionChangeObserver, VisibilityChangeObserver {
    void createEnemyUnit(List<LatLng> route, LatLng position, Units units);
    void createPlayerUnit(List<LatLng> route, LatLng position, Units units);
    List<Unit> getUnits();
    Unit getUnit(UUID id);
}
