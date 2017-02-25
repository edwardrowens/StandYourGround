package com.ede.standyourground.game.framework.render.impl;

import android.os.Handler;
import android.os.Looper;

import com.ede.standyourground.game.framework.render.api.Renderable;
import com.ede.standyourground.game.framework.render.api.Renderer;

public class RendererImpl implements Renderer {


    private Handler renderingHandler = new Handler(Looper.getMainLooper());

    public void render(final Renderable renderable) {
        renderingHandler.post(new Runnable() {
            @Override
            public void run() {
                renderable.render();
            }
        });
    }
}
