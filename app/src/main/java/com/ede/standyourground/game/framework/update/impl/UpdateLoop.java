package com.ede.standyourground.game.framework.update.impl;

import android.os.Handler;
import android.os.HandlerThread;

import com.ede.standyourground.framework.Logger;
import com.ede.standyourground.game.framework.management.impl.UnitServiceImpl;
import com.ede.standyourground.game.framework.update.service.api.UpdateService;
import com.ede.standyourground.game.model.Unit;

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
        long t = System.currentTimeMillis();
        long position = 0;
        long visibility = 0;
        logger.d("Begin update loop iter");
        List<Unit> units = unitService.get().getUnits();
        for (Unit unit : units) {

            long v = System.currentTimeMillis();
            updateService.get().determinePosition(unit);
            position += System.currentTimeMillis() - v;
            long p = System.currentTimeMillis();
            updateService.get().determineVisibility(unit);
            visibility += System.currentTimeMillis() - p;
        }
        long m = System.currentTimeMillis();
        updateService.get().processCombat(units);
        logger.d("\tcombat <%d ms>", System.currentTimeMillis()-m);

        if (loop.get()) {
            handler.postDelayed(this, LOOP_DELAY);
        }
        logger.d("\tPosition <%d ms>", position);
        logger.d("\tVisibility <%d ms>", visibility);
        logger.d("End update loop iter <%d ms>", System.currentTimeMillis()-t);
    }
}
