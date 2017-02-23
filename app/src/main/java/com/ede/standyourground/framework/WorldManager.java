package com.ede.standyourground.framework;

import com.ede.standyourground.game.model.Unit;
import com.ede.standyourground.networking.exchange.request.impl.CreateUnitRequest;
import com.ede.standyourground.networking.framework.NetworkingManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Eddie on 2/8/2017.
 */

public class WorldManager {

    private static Logger logger = new Logger(WorldManager.class);
    private static final WorldManager instance = new WorldManager();

    private UpdateLoop updateLoop = new UpdateLoop();
    private UpdateLoopHandler updateLoopHandler = new UpdateLoopHandler();
    private static Map<UUID, Unit> units = new ConcurrentHashMap<>();

    private WorldManager() {
        UpdateLoopManager.setHandler(updateLoopHandler);
        UpdateLoopManager.getInstance();
    }

    public static WorldManager getInstance() {
        return instance;
    }

    public void startLoop() {
        updateLoop.startLoop();
    }

    public void addUnit(Unit unit) {
        units.put(unit.getId(), unit);
        CreateUnitRequest createUnitRequest = new CreateUnitRequest();
        createUnitRequest.setPosition(unit.getStartingPosition());
        createUnitRequest.setWaypoints(unit.getWaypoints());
        NetworkingManager.getInstance().sendRequest(createUnitRequest);
        logger.i("Added unit. %d units managed", units.size());
    }

    public List<Unit> getUnits() {
        return new ArrayList<>(units.values());
    }

    public Unit getUnit(UUID id) {
        return units.get(id);
    }
}
