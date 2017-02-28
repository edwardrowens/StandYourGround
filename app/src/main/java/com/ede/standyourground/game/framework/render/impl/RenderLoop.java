package com.ede.standyourground.game.framework.render.impl;

import android.os.Handler;
import android.os.Looper;

import com.ede.standyourground.game.framework.management.impl.WorldManager;
import com.ede.standyourground.game.model.Unit;

import javax.inject.Inject;

import dagger.Lazy;

public class RenderLoop {

    private final Handler renderingHandler = new Handler(Looper.getMainLooper());
    private final Lazy<WorldManager> worldManager;

    @Inject
    RenderLoop(Lazy<WorldManager> worldManager) {
        this.worldManager = worldManager;
    }

    public void startLoop() {
        renderingHandler.post(new Runnable() {
            @Override
            public void run() {
                for (Unit unit : worldManager.get().getUnits().values()) {
                    unit.onRender();
                }
                renderingHandler.postDelayed(this, 16);
            }
        });
    }
}
