package com.ede.standyourground.game.framework.management.impl;

import com.ede.standyourground.framework.Logger;
import com.ede.standyourground.framework.dagger.providers.GameSessionIdProvider;
import com.ede.standyourground.game.framework.render.impl.RenderLoop;
import com.ede.standyourground.game.framework.update.impl.UpdateLoop;
import com.ede.standyourground.game.model.FootSoldier;
import com.ede.standyourground.game.model.Unit;
import com.ede.standyourground.game.model.Units;
import com.ede.standyourground.networking.exchange.request.impl.CreateUnitRequest;
import com.ede.standyourground.networking.framework.NetworkingManager;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Lazy;

@Singleton
public class WorldManager {

    private static Logger logger = new Logger(WorldManager.class);

    private Map<UUID, Unit> units = new ConcurrentHashMap<>();
    private final Lazy<UpdateLoop> updateLoop;
    private final Lazy<RenderLoop> renderLoop;
    private final Lazy<UnitCreator> unitCreator;
    private final Lazy<NetworkingManager> networkingManager;
    private final Lazy<GameSessionIdProvider> gameSessionIdProvider;

    @Inject
    WorldManager(Lazy<UpdateLoop> updateLoop,
                 Lazy<UnitCreator> unitCreator,
                 Lazy<NetworkingManager> networkingManager,
                 Lazy<GameSessionIdProvider> gameSessionIdProvider,
                 Lazy<RenderLoop> renderLoop) {
        this.updateLoop = updateLoop;
        this.unitCreator = unitCreator;
        this.networkingManager = networkingManager;
        this.gameSessionIdProvider = gameSessionIdProvider;
        this.renderLoop = renderLoop;
    }


    public void start() {
        updateLoop.get().startLoop();
        renderLoop.get().startLoop();
    }

    public void createEnemyUnit(List<LatLng> route, LatLng position, Units units) {
        unitCreator.get().createEnemyUnit(route, position, units);
    }

    public void createPlayerUnit(List<LatLng> route, LatLng position, Units units) {
        unitCreator.get().createPlayerUnit(route, position, units);
    }

    public void addPlayerUnit(Unit unit) {
        CreateUnitRequest createUnitRequest = new CreateUnitRequest();
        createUnitRequest.setPosition(unit.getStartingPosition());
        createUnitRequest.setTimestamp(unit.getCreatedTime());
        createUnitRequest.setWaypoints(unit.getWaypoints());
        createUnitRequest.setGameSessionId(gameSessionIdProvider.get().getGameSessionId());

        if (unit instanceof FootSoldier) {
            createUnitRequest.setUnit(Units.FOOT_SOLDIER);
        } else {
            createUnitRequest.setUnit(Units.FOOT_SOLDIER);
        }

        networkingManager.get().sendExchange(createUnitRequest);
        addUnit(unit);
    }

    public void addEnemyUnit(Unit unit) {
        addUnit(unit);
    }

    private void addUnit(Unit unit) {
        units.put(unit.getId(), unit);
        logger.i("Added unit. %d units managed", units.size());
    }

    public Map<UUID, Unit> getUnits() {
        return units;
    }

    public Unit getUnit(UUID id) {
        return units.get(id);
    }
}
