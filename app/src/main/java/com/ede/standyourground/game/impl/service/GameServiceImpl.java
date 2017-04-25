package com.ede.standyourground.game.impl.service;

import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.framework.api.service.DrawRouteService;
import com.ede.standyourground.framework.api.service.LatLngService;
import com.ede.standyourground.game.api.event.listener.CoinBalanceChangeListener;
import com.ede.standyourground.game.api.event.listener.GameEndListener;
import com.ede.standyourground.game.api.event.listener.OnDeathListener;
import com.ede.standyourground.game.api.model.ArtificialOpponentDifficulty;
import com.ede.standyourground.game.api.model.GameMode;
import com.ede.standyourground.game.api.model.Hostility;
import com.ede.standyourground.game.api.model.NeutralCamp;
import com.ede.standyourground.game.api.model.Player;
import com.ede.standyourground.game.api.model.Unit;
import com.ede.standyourground.game.api.model.UnitType;
import com.ede.standyourground.game.api.model.WorldGrid;
import com.ede.standyourground.game.api.service.ArtificialOpponentFactory;
import com.ede.standyourground.game.api.service.ArtificialOpponentService;
import com.ede.standyourground.game.api.service.GameService;
import com.ede.standyourground.game.api.service.PlayerService;
import com.ede.standyourground.game.api.service.UnitService;
import com.ede.standyourground.game.api.service.WorldGridService;
import com.ede.standyourground.game.impl.model.ArtificialOpponent;
import com.ede.standyourground.game.impl.model.BankNeutralCamp;
import com.ede.standyourground.game.impl.model.MedicNeutralCamp;
import com.ede.standyourground.game.impl.update.UpdateLoop;
import com.ede.standyourground.networking.api.model.Routes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Lazy;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class GameServiceImpl implements GameService {

    private static final Logger logger = new Logger(GameServiceImpl.class);

    private final Lazy<UpdateLoop> updateLoop;
    private final Lazy<UnitService> unitService;
    private final Lazy<PlayerService> playerService;
    private final Lazy<LatLngService> latLngService;
    private final Lazy<WorldGridService> worldGridService;
    private final Lazy<DrawRouteService> drawRouteService;
    private final Lazy<ArtificialOpponentFactory> artificialOpponentFactory;
    private final Lazy<ArtificialOpponentService> artificialOpponentService;
    private WorldGrid worldGrid;

    @Inject
    GameServiceImpl(Lazy<UpdateLoop> updateLoop,
                    Lazy<UnitService> unitService,
                    Lazy<PlayerService> playerService,
                    Lazy<LatLngService> latLngService,
                    Lazy<WorldGridService> worldGridService,
                    Lazy<DrawRouteService> drawRouteService,
                    Lazy<ArtificialOpponentFactory> artificialOpponentFactory,
                    Lazy<ArtificialOpponentService> artificialOpponentService) {
        this.updateLoop = updateLoop;
        this.unitService = unitService;
        this.playerService = playerService;
        this.latLngService = latLngService;
        this.worldGridService = worldGridService;
        this.drawRouteService = drawRouteService;
        this.artificialOpponentFactory = artificialOpponentFactory;
        this.artificialOpponentService = artificialOpponentService;
    }

    @Override
    public void startGame(GameMode gameMode, final LatLng playerLocation, LatLng opponentLocation) {
        worldGrid = new WorldGrid(latLngService.get().midpoint(playerLocation, opponentLocation));
        final Player player = new Player(UUID.randomUUID(), true);
        playerService.get().addPlayer(player);

        unitService.get().createFriendlyUnit(null, playerLocation, UnitType.BASE);
        unitService.get().createEnemyUnit(null, opponentLocation, UnitType.BASE);

        unitService.get().registerOnDeathListener(new OnDeathListener() {
            @Override
            public void onDeath(Unit mortal, Unit killer) {
                if (mortal instanceof NeutralCamp) {
                    logger.i("%s killed neutral camp %s. Converting camp from %s to %s", killer.getId(), mortal.getId(), mortal.getHostility(), killer.getHostility());
                    if (killer.getHostility() == Hostility.FRIENDLY) {
                        if (mortal instanceof MedicNeutralCamp) {
                            player.incrementMedicNeutralCampCount();
                        } else {
                            player.incrementBankNeutralCampCount();
                        }
                    } else if (mortal.getHostility() == Hostility.FRIENDLY) {
                        if (mortal instanceof MedicNeutralCamp) {
                            player.decrementMedicNeutralCampCount();
                        } else {
                            player.decrementBankNeutralCampCount();
                        }
                    }

                    if (mortal instanceof BankNeutralCamp) {
                        if (killer.getHostility() == Hostility.FRIENDLY) {
                            playerService.get().updateIncome(playerService.get().getMainPlayerId(), ((BankNeutralCamp) mortal).getProvidedIncome());
                        } else if (mortal.getHostility() == Hostility.FRIENDLY) {
                            playerService.get().updateIncome(playerService.get().getMainPlayerId(), -((BankNeutralCamp) mortal).getProvidedIncome());
                        }
                    }
                    unitService.get().createNeutralUnit(mortal.getStartingPosition(), mortal.getType(), ((NeutralCamp) mortal).getName(), ((NeutralCamp) mortal).getPhotoReference(), killer.getHostility());
                } else {
                    worldGridService.get().removeUnitAtCell(mortal.getCell(), mortal);
                }
            }
        });
        switch (gameMode) {
            case MULTIPLAYER:
                startMultiplayerGame(playerLocation, opponentLocation);
                break;
            case SINGLE_PLAYER:
                startSinglePlayerGame(playerLocation, opponentLocation);
                break;
        }
        updateLoop.get().startLoop();
    }

    @Override
    public void stopGame() {
        logger.i("Ending game");
        updateLoop.get().stopLoop();
    }

    @Override
    public void createEntity(final UUID playerId, final UnitType type, final LatLng start, final LatLng end, List<LatLng> intermediaryPositions) {
        drawRouteService.get().createRoutesForUnit(start, end, intermediaryPositions, new Callback<Routes>() {
            @Override
            public void onResponse(Call<Routes> call, Response<Routes> response) {
                unitService.get().createFriendlyUnit(PolyUtil.decode(response.body().getRoutes().get(0).getOverviewPolyline().getPoints()), start, type);
                playerService.get().makePurchase(playerId, type.getCost());
                // TODO DELETE
                logger.i("Creating enemy unit.");
                drawRouteService.get().createRoutesForEnemyUnit(UnitType.MARAUDER, new ArrayList<Marker>(), start, end);
                // TODO END OF DELETE
            }

            @Override
            public void onFailure(Call<Routes> call, Throwable t) {

            }
        });
    }

    @Override
    public void registerGameEndListener(GameEndListener gameEndListener) {
        unitService.get().registerGameEndListener(gameEndListener);
    }

    @Override
    public void registerCoinBalanceChangeListener(CoinBalanceChangeListener coinBalanceChangeListener) {
        playerService.get().registerCoinBalanceChangeListener(coinBalanceChangeListener);
    }

    @Override
    public WorldGrid getWorldGrid() {
        return worldGrid;
    }

    private void startMultiplayerGame(LatLng playerLocation, LatLng opponentLocation) {

    }

    private void startSinglePlayerGame(LatLng playerLocation, LatLng opponentLocation) {
        ArtificialOpponent artificialOpponent = artificialOpponentFactory.get().createArtificialOpponent(ArtificialOpponentDifficulty.EASY, opponentLocation, playerLocation);
        artificialOpponentService.get().runArtificialOpponent(artificialOpponent);
    }
}
