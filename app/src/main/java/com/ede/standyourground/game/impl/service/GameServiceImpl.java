package com.ede.standyourground.game.impl.service;

import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.game.api.event.listener.GameEndListener;
import com.ede.standyourground.game.api.event.listener.OnDeathListener;
import com.ede.standyourground.game.api.model.NeutralCamp;
import com.ede.standyourground.game.api.model.Unit;
import com.ede.standyourground.game.api.service.GameService;
import com.ede.standyourground.game.api.service.UnitService;
import com.ede.standyourground.game.impl.update.UpdateLoop;

import javax.inject.Inject;

import dagger.Lazy;

public class GameServiceImpl implements GameService {

    private static final Logger logger = new Logger(GameServiceImpl.class);

    private final Lazy<UpdateLoop> updateLoop;
    private final Lazy<UnitService> unitService;

    @Inject
    GameServiceImpl(Lazy<UpdateLoop> updateLoop,
                    Lazy<UnitService> unitService) {
        this.updateLoop = updateLoop;
        this.unitService = unitService;
    }

    @Override
    public void startGame() {
        unitService.get().registerOnDeathListener(new OnDeathListener() {
            @Override
            public void onDeath(Unit mortal, Unit killer) {
                if (mortal instanceof NeutralCamp) {
                    logger.i("%s killed neutral camp %s. Converting camp from %s to %s", killer.getId(), mortal.getId(), mortal.getHostility(), killer.getHostility());
                    unitService.get().createNeutralUnit(mortal.getStartingPosition(), mortal.getType(), ((NeutralCamp) mortal).getName(), ((NeutralCamp) mortal).getPhotoReference(), killer.getHostility());
                }
            }
        });
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
}
