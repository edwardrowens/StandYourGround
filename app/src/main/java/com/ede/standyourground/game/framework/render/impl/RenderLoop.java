package com.ede.standyourground.game.framework.render.impl;

import android.os.Handler;
import android.os.Looper;

import com.ede.standyourground.framework.Logger;
import com.ede.standyourground.game.framework.management.impl.WorldManager;
import com.ede.standyourground.game.framework.render.api.RenderService;
import com.ede.standyourground.game.model.Unit;

import javax.inject.Inject;

import dagger.Lazy;

public class RenderLoop {

    private static final Logger logger = new Logger(RenderLoop.class);

    private final Handler renderingHandler = new Handler(Looper.getMainLooper());
    private final Lazy<WorldManager> worldManager;
    private final Lazy<RenderService> renderService;

    @Inject
    RenderLoop(Lazy<WorldManager> worldManager,
               Lazy<RenderService> renderService) {
        this.worldManager = worldManager;
        this.renderService = renderService;
    }

    public void startLoop() {
        renderingHandler.post(new Runnable() {
            @Override
            public void run() {
                for (Unit unit : worldManager.get().getUnits().values()) {
                    unit.onRender();
                    renderService.get().renderHealthBar(unit);
                }
                renderingHandler.postDelayed(this, 16);
            }
        });
    }

    public void stopLoop() {
        logger.i("Stopping rendering loop");
    }
}
