package com.ede.standyourground.framework.dagger.module;

import com.ede.standyourground.app.service.api.DirectionsService;
import com.ede.standyourground.app.service.api.DrawRouteService;
import com.ede.standyourground.app.service.api.MapSetupService;
import com.ede.standyourground.app.service.impl.DirectionsServiceImpl;
import com.ede.standyourground.app.service.impl.DrawRouteServiceImpl;
import com.ede.standyourground.app.service.impl.MapSetupServiceImpl;
import com.ede.standyourground.framework.api.LatLngService;
import com.ede.standyourground.framework.api.MathService;
import com.ede.standyourground.framework.api.RouteService;
import com.ede.standyourground.framework.impl.LatLngServiceImpl;
import com.ede.standyourground.framework.impl.MathServiceImpl;
import com.ede.standyourground.framework.impl.RouteServiceImpl;
import com.ede.standyourground.game.framework.management.api.GameService;
import com.ede.standyourground.game.framework.management.api.UnitService;
import com.ede.standyourground.game.framework.management.impl.GameServiceImpl;
import com.ede.standyourground.game.framework.management.impl.UnitServiceImpl;
import com.ede.standyourground.game.framework.render.api.RenderService;
import com.ede.standyourground.game.framework.render.impl.RenderServiceImpl;
import com.ede.standyourground.game.framework.update.service.api.UpdateService;
import com.ede.standyourground.game.framework.update.service.impl.UpdateServiceImpl;
import com.ede.standyourground.networking.framework.api.NetworkingManager;
import com.ede.standyourground.networking.framework.impl.MockNetworkingManager;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ServiceModule {

    @Binds public abstract UpdateService bindUpdateService(UpdateServiceImpl updateServiceImpl);

    @Binds public abstract RouteService bindRouteService(RouteServiceImpl routeServiceImpl);

    @Binds public abstract LatLngService bindLatLngService(LatLngServiceImpl latLngServiceImpl);

    @Binds public abstract NetworkingManager bindNetworkingManager(MockNetworkingManager networkingManager);

    @Binds public abstract RenderService bindRenderService(RenderServiceImpl renderService);

    @Binds public abstract MapSetupService bindOnMapReadyService(MapSetupServiceImpl onMapReadyService);

    @Binds public abstract MathService bindMathService(MathServiceImpl mathService);

    @Binds public abstract DirectionsService bindDirectionsService(DirectionsServiceImpl directionsService);

    @Binds public abstract DrawRouteService bindDrawRouteService(DrawRouteServiceImpl drawRouteService);

    @Binds public abstract UnitService bindUnitService(UnitServiceImpl unitServiceImpl);

    @Binds public abstract GameService bindGameService(GameServiceImpl gameService);
}
