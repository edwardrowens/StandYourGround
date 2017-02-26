package com.ede.standyourground.game.framework.update.impl;

import android.os.Handler;
import android.os.HandlerThread;

import com.ede.standyourground.framework.Logger;
import com.ede.standyourground.game.framework.management.impl.WorldManager;
import com.ede.standyourground.game.framework.render.api.Renderer;
import com.ede.standyourground.game.framework.update.service.api.UpdateService;
import com.ede.standyourground.game.model.Unit;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Lazy;

@Singleton
public class UpdateLoop implements Runnable {

    private static final Logger logger = new Logger(UpdateLoop.class);
    private static final long LOOP_DELAY = 16;

    private final HandlerThread loopThread = new HandlerThread("UpdateLoop");
    private Handler handler;

    private final Lazy<UpdateService> updateService;
    private final Lazy<WorldManager> worldManager;
    private final Lazy<Renderer> renderer;

    @Inject
    UpdateLoop(Lazy<UpdateService> updateService,
               Lazy<WorldManager> worldManager,
               Lazy<Renderer> renderer) {
        this.updateService = updateService;
        this.worldManager = worldManager;
        this.renderer = renderer;
    }

    public void startLoop() {
        logger.i("Starting update thread");
        loopThread.start();
        handler = new Handler(loopThread.getLooper());
        handler.post(this);
    }

    @Override
    public void run() {
        for (Unit unit : worldManager.get().getUnits()) {
            updateService.get().determinePosition(unit);
            updateService.get().determineVisibility(worldManager.get().getUnits());
            renderer.get().render(unit);
        }

        handler.postDelayed(this, LOOP_DELAY);
    }
}
