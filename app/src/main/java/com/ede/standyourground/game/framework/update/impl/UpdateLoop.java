package com.ede.standyourground.game.framework.update.impl;

import android.os.Handler;
import android.os.HandlerThread;

import com.ede.standyourground.framework.Logger;
import com.ede.standyourground.game.framework.management.impl.WorldManager;
import com.ede.standyourground.game.framework.update.service.api.UpdateService;
import com.ede.standyourground.game.model.Unit;

import java.util.Collection;

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

    @Inject
    UpdateLoop(Lazy<UpdateService> updateService,
               Lazy<WorldManager> worldManager) {
        this.updateService = updateService;
        this.worldManager = worldManager;
    }

    public void startLoop() {
        logger.i("Starting update thread");
        loopThread.start();
        handler = new Handler(loopThread.getLooper());
        handler.post(this);
    }

    @Override
    public void run() {
        Collection<Unit> units = worldManager.get().getUnits().values();
        for (Unit unit : units) {
            updateService.get().determinePosition(unit);
            updateService.get().determineVisibility(unit);
        }
        updateService.get().processCombat(units);

        handler.postDelayed(this, LOOP_DELAY);
    }
}