package com.ede.standyourground.framework.modules;

import com.ede.standyourground.framework.api.MathService;
import com.ede.standyourground.framework.api.RouteService;
import com.ede.standyourground.framework.impl.MathServiceImpl;
import com.ede.standyourground.framework.impl.RouteServiceImpl;
import com.ede.standyourground.game.framework.render.api.Renderer;
import com.ede.standyourground.game.framework.render.impl.RendererImpl;
import com.ede.standyourground.game.framework.update.service.api.UpdateService;
import com.ede.standyourground.game.framework.update.service.impl.UpdateServiceImpl;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ServiceModule {

    @Binds
    public abstract UpdateService bindUpdateService(UpdateServiceImpl updateServiceImpl);

    @Binds
    public abstract RouteService bindRouteService(RouteServiceImpl routeServiceImpl);

    @Binds
    public abstract MathService bindMathService(MathServiceImpl mathServiceImpl);

    @Binds
    public abstract Renderer bindRenderer(RendererImpl rendererImpl);
}
