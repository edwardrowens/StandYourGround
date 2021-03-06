package com.ede.standyourground.game.api.service;

import com.ede.standyourground.game.api.event.observer.BankNeutralCampIncomeObserver;
import com.ede.standyourground.game.api.event.observer.DeathObserver;
import com.ede.standyourground.game.api.event.observer.GameEndObserver;
import com.ede.standyourground.game.api.event.observer.HealthChangeObserver;
import com.ede.standyourground.game.api.event.observer.PositionChangeObserver;
import com.ede.standyourground.game.api.event.observer.UnitCreatedObserver;
import com.ede.standyourground.game.api.event.observer.VisibilityChangeObserver;
import com.ede.standyourground.game.api.model.Hostility;
import com.ede.standyourground.game.api.model.Unit;
import com.ede.standyourground.game.api.model.UnitType;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.UUID;

/**
 *
 */
public interface UnitService extends DeathObserver, HealthChangeObserver, GameEndObserver, UnitCreatedObserver, PositionChangeObserver, VisibilityChangeObserver, BankNeutralCampIncomeObserver {
    void createEnemyUnit(List<LatLng> route, LatLng position, UnitType unitType);
    void createFriendlyUnit(List<LatLng> route, LatLng position, UnitType unitType);
    void createNeutralUnit(LatLng position, UnitType unitType, String name, String photoReference, Hostility hostility);
    List<Unit> getUnits();
    Unit getUnit(UUID id);
}
