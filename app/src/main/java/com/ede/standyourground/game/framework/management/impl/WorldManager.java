package com.ede.standyourground.game.framework.management.impl;

import com.ede.standyourground.framework.Logger;
import com.ede.standyourground.game.framework.render.api.Renderer;
import com.ede.standyourground.game.framework.render.impl.RendererImpl;
import com.ede.standyourground.game.framework.update.impl.UpdateLoop;
import com.ede.standyourground.game.framework.update.impl.UpdateLoopHandler;
import com.ede.standyourground.game.framework.update.impl.UpdateLoopManager;
import com.ede.standyourground.game.model.FootSoldier;
import com.ede.standyourground.game.model.Unit;
import com.ede.standyourground.game.model.Units;
import com.ede.standyourground.networking.exchange.request.impl.CreateUnitRequest;
import com.ede.standyourground.networking.framework.NetworkingManager;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class WorldManager {

    private static Logger logger = new Logger(WorldManager.class);
    private static final WorldManager instance = new WorldManager();

    private Renderer renderer;
    private UnitCreator unitCreator;

    private UpdateLoop updateLoop = new UpdateLoop();
    private UpdateLoopHandler updateLoopHandler = new UpdateLoopHandler();

    private Map<UUID, Unit> units = new ConcurrentHashMap<>();
    private UUID gameSessionId;

    private WorldManager() {
        UpdateLoopManager.setHandler(updateLoopHandler);
        UpdateLoopManager.getInstance();
    }

    public static WorldManager getInstance() {
        return instance;
    }

    public void start(GoogleMap googleMap, UUID gameSessionId) {
        this.gameSessionId = gameSessionId;
        unitCreator = new UnitCreator(googleMap);
        this.renderer = new RendererImpl();
        updateLoop.startLoop(renderer);
    }

    public void createUnit(List<LatLng> route, LatLng position, Units units, boolean notifyOpponent) {
        if (notifyOpponent) {
            unitCreator.createUnit(route, position, units, gameSessionId);
        } else {
            unitCreator.createUnit(route, position, units);
        }
    }

    public void addUnit(Unit unit, UUID gameSessionId) {
        CreateUnitRequest createUnitRequest = new CreateUnitRequest();
        createUnitRequest.setPosition(unit.getStartingPosition());
        createUnitRequest.setTimestamp(unit.getCreatedTime());
        createUnitRequest.setWaypoints(unit.getWaypoints());
        createUnitRequest.setGameSessionId(gameSessionId);

        if (unit instanceof FootSoldier) {
            createUnitRequest.setUnit(Units.FOOT_SOLDIER);
        } else {
            createUnitRequest.setUnit(Units.FOOT_SOLDIER);
        }

        NetworkingManager.getInstance().sendExchange(createUnitRequest);
        addUnit(unit);
    }

    public void addUnit(Unit unit) {
        units.put(unit.getId(), unit);
        logger.i("Added unit. %d units managed", units.size());
    }

    public List<Unit> getUnits() {
        return new ArrayList<>(units.values());
    }

    public Unit getUnit(UUID id) {
        return units.get(id);
    }
}
