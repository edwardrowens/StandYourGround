package com.ede.standyourground.game.impl.service;

import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.framework.api.dagger.providers.GameSessionIdProvider;
import com.ede.standyourground.game.api.event.listener.GameEndListener;
import com.ede.standyourground.game.api.event.listener.HealthChangeListener;
import com.ede.standyourground.game.api.event.listener.OnDeathListener;
import com.ede.standyourground.game.api.event.listener.PositionChangeListener;
import com.ede.standyourground.game.api.event.listener.UnitCreatedListener;
import com.ede.standyourground.game.api.event.listener.VisibilityChangeListener;
import com.ede.standyourground.game.api.model.Hostility;
import com.ede.standyourground.game.api.model.MovableUnit;
import com.ede.standyourground.game.api.model.Unit;
import com.ede.standyourground.game.api.model.Units;
import com.ede.standyourground.game.api.service.UnitService;
import com.ede.standyourground.game.impl.model.Base;
import com.ede.standyourground.game.impl.model.FootSoldier;
import com.ede.standyourground.networking.api.NetworkingHandler;
import com.ede.standyourground.networking.api.exchange.payload.request.CreateUnitRequest;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Lazy;

@Singleton
public class UnitServiceImpl implements UnitService {

    private static Logger logger = new Logger(UnitServiceImpl.class);

    private Map<UUID, Unit> units = new ConcurrentHashMap<>();
    private final Lazy<UnitFactory> unitCreator;
    private final Lazy<NetworkingHandler> networkingManager;
    private final Lazy<GameSessionIdProvider> gameSessionIdProvider;

    // Listeners
    private final List<OnDeathListener> onDeathListeners = new CopyOnWriteArrayList<>();
    private final List<HealthChangeListener> healthChangeListeners = new CopyOnWriteArrayList<>();
    private final List<GameEndListener> gameEndListeners = new CopyOnWriteArrayList<>();
    private final List<UnitCreatedListener> unitCreatedListeners = new CopyOnWriteArrayList<>();
    private final List<PositionChangeListener> positionChangeListeners = new CopyOnWriteArrayList<>();
    private final List<VisibilityChangeListener> visibilityChangeListeners = new CopyOnWriteArrayList<>();

    @Inject
    UnitServiceImpl(Lazy<UnitFactory> unitFactory,
                    Lazy<NetworkingHandler> networkingManager,
                    Lazy<GameSessionIdProvider> gameSessionIdProvider) {
        this.unitCreator = unitFactory;
        this.networkingManager = networkingManager;
        this.gameSessionIdProvider = gameSessionIdProvider;
    }

    @Override
    public void createEnemyUnit(List<LatLng> route, LatLng position, Units units) {
        Unit unit = unitCreator.get().createEnemyUnit(route, position, units);
        addEnemyUnit(unit);
    }

    @Override
    public void createFriendlyUnit(List<LatLng> route, LatLng position, Units units) {
        Unit unit = unitCreator.get().createPlayerUnit(route, position, units);
        addPlayerUnit(unit);
    }

    @Override
    public void createNeutralUnit(LatLng position, Units units, String name, String photoReference, final Hostility hostility) {
        Unit unit = unitCreator.get().createNeutralUnit(position, units, name, photoReference, hostility);
        addUnit(unit);
    }

    @Override
    public List<Unit> getUnits() {
        return new ArrayList<>(units.values());
    }

    @Override
    public Unit getUnit(UUID id) {
        return units.get(id);
    }

    @Override
    public void registerOnDeathListener(OnDeathListener onDeathListener) {
        onDeathListeners.add(onDeathListener);
    }

    @Override
    public void removeOnDeathListener(OnDeathListener onDeathListener) {
        onDeathListeners.remove(onDeathListener);
    }

    @Override
    public void registerHealthChangeListener(HealthChangeListener healthChangeListener) {
        healthChangeListeners.add(healthChangeListener);
    }

    @Override
    public void registerGameEndListener(GameEndListener gameEndListener) {
        gameEndListeners.add(gameEndListener);
    }

    @Override
    public void registerUnitCreatedListener(UnitCreatedListener unitCreatedListener) {
        unitCreatedListeners.add(unitCreatedListener);
    }

    @Override
    public void registerPositionChangeListener(PositionChangeListener positionChangeListener) {
        positionChangeListeners.add(positionChangeListener);
    }

    @Override
    public void registerVisibilityChangeListener(VisibilityChangeListener visibilityChangeListener) {
        visibilityChangeListeners.add(visibilityChangeListener);
    }

    private void addPlayerUnit(final Unit unit) {
        sendCreateUnitRequest(unit);
        addUnit(unit);
    }

    private void addEnemyUnit(Unit unit) {
        addUnit(unit);
    }

    private void addUnit(final Unit unit) {
        unit.registerOnDeathListener(new OnDeathListener() {
            @Override
            public void onDeath(Unit mortal, Unit killer) {
                if (mortal instanceof Base) {
                    for (GameEndListener gameEndListener : gameEndListeners) {
                        gameEndListener.onGameEnd(mortal.getHostility() == Hostility.ENEMY);
                    }
                }
                if (units.remove(mortal.getId()) != null) {
                    logger.i("Unit %s has been removed from the game.", mortal.getId());
                } else {
                    logger.w("Unit %s was not contained within the game.", mortal.getId());
                }

                for (OnDeathListener onDeathListener : onDeathListeners) {
                    onDeathListener.onDeath(unit, killer);
                }
            }
        });
        unit.registerHealthChangeListener(new HealthChangeListener() {
            @Override
            public void onHealthChange(Unit unit) {
                for (HealthChangeListener healthChangeListener : healthChangeListeners) {
                    healthChangeListener.onHealthChange(unit);
                }
            }
        });
        unit.registerVisibilityChangeListener(new VisibilityChangeListener() {
            @Override
            public void onVisibilityChange(Unit unit) {
                for (VisibilityChangeListener visibilityChangeListener : visibilityChangeListeners) {
                    visibilityChangeListener.onVisibilityChange(unit);
                }
            }
        });
        if (unit instanceof MovableUnit) {
            ((MovableUnit) unit).registerPositionChangeListener(new PositionChangeListener() {
                @Override
                public void onPositionChange(MovableUnit movableUnit) {
                    for (PositionChangeListener positionChangeListener : positionChangeListeners) {
                        positionChangeListener.onPositionChange(movableUnit);
                    }
                }
            });
        }
        units.put(unit.getId(), unit);
        logger.i("Added unit. %d units managed", units.size());

        for(UnitCreatedListener unitCreatedListener : unitCreatedListeners) {
            unitCreatedListener.onUnitCreated(unit);
        }
    }

    private void sendCreateUnitRequest(Unit unit) {
        CreateUnitRequest createUnitRequest = new CreateUnitRequest();
        createUnitRequest.setPosition(unit.getStartingPosition());
        createUnitRequest.setTimestamp(unit.getCreatedTime());
        if (unit instanceof MovableUnit) {
            createUnitRequest.setWaypoints(((MovableUnit)unit).getWaypoints());
        }
        createUnitRequest.setGameSessionId(gameSessionIdProvider.get().getGameSessionId());

        if (unit instanceof FootSoldier) {
            createUnitRequest.setUnit(Units.FOOT_SOLDIER);
        } else if (unit instanceof Base) {
            createUnitRequest.setUnit(Units.BASE);
        }
        networkingManager.get().sendExchange(createUnitRequest);
    }
}
