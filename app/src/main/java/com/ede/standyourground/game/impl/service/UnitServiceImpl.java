package com.ede.standyourground.game.impl.service;

import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.framework.api.dagger.providers.GameSessionIdProvider;
import com.ede.standyourground.game.api.event.listener.BankNeutralCampIncomeListener;
import com.ede.standyourground.game.api.event.listener.GameEndListener;
import com.ede.standyourground.game.api.event.listener.HealthChangeListener;
import com.ede.standyourground.game.api.event.listener.OnDeathListener;
import com.ede.standyourground.game.api.event.listener.PositionChangeListener;
import com.ede.standyourground.game.api.event.listener.UnitCreatedListener;
import com.ede.standyourground.game.api.event.listener.VisibilityChangeListener;
import com.ede.standyourground.game.api.model.Hostility;
import com.ede.standyourground.game.api.model.MovableUnit;
import com.ede.standyourground.game.api.model.Unit;
import com.ede.standyourground.game.api.model.UnitType;
import com.ede.standyourground.game.api.service.PlayerService;
import com.ede.standyourground.game.api.service.UnitService;
import com.ede.standyourground.game.api.service.WorldGridService;
import com.ede.standyourground.game.impl.model.BankNeutralCamp;
import com.ede.standyourground.game.impl.model.Base;
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
    private final Lazy<PlayerService> playerService;
    private final Lazy<WorldGridService> worldGridService;

    // Listeners
    private final List<OnDeathListener> onDeathListeners = new CopyOnWriteArrayList<>();
    private final List<HealthChangeListener> healthChangeListeners = new CopyOnWriteArrayList<>();
    private final List<GameEndListener> gameEndListeners = new CopyOnWriteArrayList<>();
    private final List<UnitCreatedListener> unitCreatedListeners = new CopyOnWriteArrayList<>();
    private final List<PositionChangeListener> positionChangeListeners = new CopyOnWriteArrayList<>();
    private final List<VisibilityChangeListener> visibilityChangeListeners = new CopyOnWriteArrayList<>();
    private final List<BankNeutralCampIncomeListener> bankNeutralCampIncomeListeners = new CopyOnWriteArrayList<>();

    @Inject
    UnitServiceImpl(Lazy<UnitFactory> unitFactory,
                    Lazy<NetworkingHandler> networkingManager,
                    Lazy<GameSessionIdProvider> gameSessionIdProvider,
                    Lazy<PlayerService> playerService,
                    Lazy<WorldGridService> worldGridService) {
        this.unitCreator = unitFactory;
        this.networkingManager = networkingManager;
        this.gameSessionIdProvider = gameSessionIdProvider;
        this.playerService = playerService;
        this.worldGridService = worldGridService;
    }

    @Override
    public void createEnemyUnit(List<LatLng> route, LatLng position, UnitType unitType) {
        Unit unit = unitCreator.get().createEnemyUnit(route, position, unitType);
        addEnemyUnit(unit);
    }

    @Override
    public void createFriendlyUnit(List<LatLng> route, LatLng position, UnitType unitType) {
        Unit unit = unitCreator.get().createPlayerUnit(route, position, unitType);
        addPlayerUnit(unit);
    }

    @Override
    public void createNeutralUnit(LatLng position, UnitType unitType, String name, String photoReference, final Hostility hostility) {
        Unit unit = unitCreator.get().createNeutralUnit(position, unitType, name, photoReference, hostility);
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

    @Override
    public void registerBankNeutralCampIncomeListener(BankNeutralCampIncomeListener bankNeutralCampIncomeListener) {
        bankNeutralCampIncomeListeners.add(bankNeutralCampIncomeListener);
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

                for (OnDeathListener onDeathListener : onDeathListeners) {
                    onDeathListener.onDeath(mortal, killer);
                }

                worldGridService.get().removeUnitAtCell(mortal.getCell(), mortal);

                if (units.remove(mortal.getId()) != null) {
                    logger.i("Unit %s has been removed from the game.", mortal.getId());
                } else {
                    logger.w("Unit %s was not contained within the game.", mortal.getId());
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
        if (unit instanceof BankNeutralCamp && unit.getHostility() == Hostility.FRIENDLY) {
            playerService.get().registerIncomeAccruedListener((BankNeutralCamp) unit);
            ((BankNeutralCamp) unit).registerBankNeutralCampIncomeListener(new BankNeutralCampIncomeListener() {
                @Override
                public void onBankNeutralCampIncome(BankNeutralCamp bank) {
                    for (BankNeutralCampIncomeListener bankNeutralCampIncomeListener : bankNeutralCampIncomeListeners) {
                        bankNeutralCampIncomeListener.onBankNeutralCampIncome(bank);
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
        createUnitRequest.setUnit(unit.getType());

        networkingManager.get().sendExchange(createUnitRequest);
    }
}
