package com.ede.standyourground.game.impl.service;

import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.game.api.event.listener.CoinBalanceChangeListener;
import com.ede.standyourground.game.api.event.listener.GameEndListener;
import com.ede.standyourground.game.api.event.listener.OnDeathListener;
import com.ede.standyourground.game.api.model.Hostility;
import com.ede.standyourground.game.api.model.NeutralCamp;
import com.ede.standyourground.game.api.model.Player;
import com.ede.standyourground.game.api.model.Unit;
import com.ede.standyourground.game.api.model.Units;
import com.ede.standyourground.game.api.service.GameService;
import com.ede.standyourground.game.api.service.PlayerService;
import com.ede.standyourground.game.api.service.UnitService;
import com.ede.standyourground.game.impl.model.BankNeutralCamp;
import com.ede.standyourground.game.impl.update.UpdateLoop;
import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Lazy;

@Singleton
public class GameServiceImpl implements GameService {

    private static final Logger logger = new Logger(GameServiceImpl.class);

    private final Lazy<UpdateLoop> updateLoop;
    private final Lazy<UnitService> unitService;
    private final Lazy<PlayerService> playerService;

    @Inject
    GameServiceImpl(Lazy<UpdateLoop> updateLoop,
                    Lazy<UnitService> unitService,
                    Lazy<PlayerService> playerService) {
        this.updateLoop = updateLoop;
        this.unitService = unitService;
        this.playerService = playerService;
    }

    @Override
    public void startGame(final LatLng playerLocation, LatLng opponentLocation) {
        unitService.get().registerOnDeathListener(new OnDeathListener() {
            @Override
            public void onDeath(Unit mortal, Unit killer) {
                if (mortal instanceof NeutralCamp) {
                    logger.i("%s killed neutral camp %s. Converting camp from %s to %s", killer.getId(), mortal.getId(), mortal.getHostility(), killer.getHostility());
                    if (mortal instanceof BankNeutralCamp) {
                        if (killer.getHostility() == Hostility.FRIENDLY) {
                            playerService.get().updateIncome(((BankNeutralCamp) mortal).getProvidedIncome());
                        } else if (mortal.getHostility() == Hostility.FRIENDLY) {
                            playerService.get().updateIncome(-((BankNeutralCamp) mortal).getProvidedIncome());
                        }
                    }
                    unitService.get().createNeutralUnit(mortal.getStartingPosition(), mortal.getType(), ((NeutralCamp) mortal).getName(), ((NeutralCamp) mortal).getPhotoReference(), killer.getHostility());
                }
            }
        });

        unitService.get().createFriendlyUnit(null, playerLocation, Units.BASE);
        unitService.get().createEnemyUnit(null, opponentLocation, Units.BASE);

        Player player = new Player(200);
        player.setId(UUID.randomUUID());
        playerService.get().addPlayer(player);

        updateLoop.get().startLoop();
    }

    @Override
    public void stopGame() {
        logger.i("Ending game");
        updateLoop.get().stopLoop();
    }

    @Override
    public void registerGameEndListener(GameEndListener gameEndListener) {
        unitService.get().registerGameEndListener(gameEndListener);
    }

    @Override
    public void registerCoinBalanceChangeListener(CoinBalanceChangeListener coinBalanceChangeListener) {
        playerService.get().registerCoinBalanceChangeListener(coinBalanceChangeListener);
    }
}
