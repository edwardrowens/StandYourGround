package com.ede.standyourground.game.impl.update;

import android.os.Handler;
import android.os.HandlerThread;

import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.game.api.model.Unit;
import com.ede.standyourground.game.api.service.UpdateService;
import com.ede.standyourground.game.impl.service.UnitServiceImpl;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Lazy;

@Singleton
public class UpdateLoop implements Runnable {

    private static final Logger logger = new Logger(UpdateLoop.class);
    private static final long LOOP_DELAY = 16;

    private final AtomicBoolean loop = new AtomicBoolean();

    private HandlerThread loopThread = new HandlerThread("UpdateLoop");
    private Handler handler;

    private final Lazy<UpdateService> updateService;
    private final Lazy<UnitServiceImpl> unitService;

    @Inject
    UpdateLoop(Lazy<UpdateService> updateService,
               Lazy<UnitServiceImpl> unitService) {
        this.updateService = updateService;
        this.unitService = unitService;
    }

    public void startLoop() {
        logger.i("Starting update loop");
        loop.set(true);
        if (!loopThread.isAlive()) {
            loopThread.start();
            handler = new Handler(loopThread.getLooper());
        }
        handler.post(this);
    }

    public void stopLoop() {
        logger.i("Stopping update loop");
        loop.set(false);
    }

    @Override
    public void run() {
        List<Unit> units = unitService.get().getUnits();
        for (Unit unit : units) {

            updateService.get().determinePosition(unit);
            updateService.get().determineVisibility(unit);
            updateService.get().calculateResourceAccrual();
        }
        updateService.get().processCombat(units);

        if (loop.get()) {
            handler.postDelayed(this, LOOP_DELAY);
        }
    }
}
