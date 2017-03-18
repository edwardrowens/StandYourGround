package com.ede.standyourground.game.framework.management.impl;

import com.ede.standyourground.framework.Logger;
import com.ede.standyourground.game.framework.management.api.GameService;
import com.ede.standyourground.game.framework.management.api.UnitService;
import com.ede.standyourground.game.framework.render.impl.RenderLoop;
import com.ede.standyourground.game.framework.update.impl.UpdateLoop;
import com.ede.standyourground.game.model.api.GameEndListener;

import javax.inject.Inject;

import dagger.Lazy;

public class GameServiceImpl implements GameService {

    private static final Logger logger = new Logger(GameServiceImpl.class);

    private final Lazy<UpdateLoop> updateLoop;
    private final Lazy<RenderLoop> renderLoop;
    private final Lazy<UnitService> unitService;

    @Inject
    GameServiceImpl(Lazy<UpdateLoop> updateLoop,
                    Lazy<RenderLoop> renderLoop,
                    Lazy<UnitService> unitService) {
        this.updateLoop = updateLoop;
        this.renderLoop = renderLoop;
        this.unitService = unitService;
    }

    @Override
    public void startGame() {
        updateLoop.get().startLoop();
        renderLoop.get().startLoop();
    }

    @Override
    public void stopGame() {
        logger.i("Ending game");
        updateLoop.get().stopLoop();
        renderLoop.get().stopLoop();
    }

    @Override
    public void registerGameEndListener(GameEndListener gameEndListener) {
        unitService.get().registerGameEndListener(gameEndListener);
    }
}
