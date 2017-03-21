package com.ede.standyourground.framework.api.dagger.module;

import com.ede.standyourground.app.ui.api.service.HealthBarService;
import com.ede.standyourground.app.ui.impl.service.HealthBarServiceImpl;
import com.ede.standyourground.framework.api.service.DirectionsService;
import com.ede.standyourground.framework.api.service.DrawRouteService;
import com.ede.standyourground.framework.api.service.LatLngService;
import com.ede.standyourground.framework.api.service.MathService;
import com.ede.standyourground.framework.api.service.RouteService;
import com.ede.standyourground.framework.impl.service.DirectionsServiceImpl;
import com.ede.standyourground.framework.impl.service.DrawRouteServiceImpl;
import com.ede.standyourground.framework.impl.service.LatLngServiceImpl;
import com.ede.standyourground.framework.impl.service.MathServiceImpl;
import com.ede.standyourground.framework.impl.service.RouteServiceImpl;
import com.ede.standyourground.game.api.service.GameService;
import com.ede.standyourground.game.api.service.UnitService;
import com.ede.standyourground.game.api.service.UpdateService;
import com.ede.standyourground.game.impl.service.GameServiceImpl;
import com.ede.standyourground.game.impl.service.UnitServiceImpl;
import com.ede.standyourground.game.impl.service.UpdateServiceImpl;
import com.ede.standyourground.networking.api.NetworkingHandler;
import com.ede.standyourground.networking.impl.MockNetworkingHandler;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ServiceModule {

    @Binds public abstract UpdateService bindUpdateService(UpdateServiceImpl updateServiceImpl);

    @Binds public abstract RouteService bindRouteService(RouteServiceImpl routeServiceImpl);

    @Binds public abstract LatLngService bindLatLngService(LatLngServiceImpl latLngServiceImpl);

    @Binds public abstract NetworkingHandler bindNetworkingManager(MockNetworkingHandler networkingManager);

    @Binds public abstract MathService bindMathService(MathServiceImpl mathService);

    @Binds public abstract DirectionsService bindDirectionsService(DirectionsServiceImpl directionsService);

    @Binds public abstract DrawRouteService bindDrawRouteService(DrawRouteServiceImpl drawRouteService);

    @Binds public abstract UnitService bindUnitService(UnitServiceImpl unitServiceImpl);

    @Binds public abstract GameService bindGameService(GameServiceImpl gameService);

    @Binds public abstract HealthBarService bindHealthBarService(HealthBarServiceImpl healthBarService);
}
