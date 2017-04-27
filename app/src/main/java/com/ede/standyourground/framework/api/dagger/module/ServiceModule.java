package com.ede.standyourground.framework.api.dagger.module;

import com.ede.standyourground.app.ui.api.service.HealthBarService;
import com.ede.standyourground.app.ui.api.service.UnitChoicesMenuService;
import com.ede.standyourground.app.ui.impl.service.HealthBarServiceImpl;
import com.ede.standyourground.app.ui.impl.service.UnitChoicesMenuServiceImpl;
import com.ede.standyourground.framework.api.service.DirectionsService;
import com.ede.standyourground.framework.api.service.DrawRouteService;
import com.ede.standyourground.framework.api.service.GraphicService;
import com.ede.standyourground.framework.api.service.LatLngService;
import com.ede.standyourground.framework.api.service.MathService;
import com.ede.standyourground.framework.api.service.ProjectionService;
import com.ede.standyourground.framework.api.service.RouteService;
import com.ede.standyourground.framework.impl.service.DirectionsServiceImpl;
import com.ede.standyourground.framework.impl.service.DrawRouteServiceImpl;
import com.ede.standyourground.framework.impl.service.GraphicServiceImpl;
import com.ede.standyourground.framework.impl.service.LatLngServiceImpl;
import com.ede.standyourground.framework.impl.service.MathServiceImpl;
import com.ede.standyourground.framework.impl.service.ProjectionServiceImpl;
import com.ede.standyourground.framework.impl.service.RouteServiceImpl;
import com.ede.standyourground.game.api.service.ArtificialOpponentService;
import com.ede.standyourground.game.api.service.GameService;
import com.ede.standyourground.game.api.service.NeutralCampService;
import com.ede.standyourground.game.api.service.PlayerService;
import com.ede.standyourground.game.api.service.UnitService;
import com.ede.standyourground.game.api.service.UpdateService;
import com.ede.standyourground.game.api.service.WorldGridService;
import com.ede.standyourground.game.impl.service.ArtificialOpponentServiceImpl;
import com.ede.standyourground.game.impl.service.GameServiceImpl;
import com.ede.standyourground.game.impl.service.NeutralCampServiceImpl;
import com.ede.standyourground.game.impl.service.PlayerServiceImpl;
import com.ede.standyourground.game.impl.service.UnitServiceImpl;
import com.ede.standyourground.game.impl.service.UpdateServiceImpl;
import com.ede.standyourground.game.impl.service.WorldGridServiceImpl;
import com.ede.standyourground.networking.api.service.GooglePlacesNearbySearchService;
import com.ede.standyourground.networking.api.service.GooglePlacesService;
import com.ede.standyourground.networking.impl.service.GooglePlacesNearbySearchServiceImpl;
import com.ede.standyourground.networking.impl.service.GooglePlacesServiceImpl;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ServiceModule {

    @Binds public abstract UpdateService bindUpdateService(UpdateServiceImpl updateServiceImpl);

    @Binds public abstract RouteService bindRouteService(RouteServiceImpl routeServiceImpl);

    @Binds public abstract LatLngService bindLatLngService(LatLngServiceImpl latLngServiceImpl);

    @Binds public abstract MathService bindMathService(MathServiceImpl mathService);

    @Binds public abstract DirectionsService bindDirectionsService(DirectionsServiceImpl directionsService);

    @Binds public abstract DrawRouteService bindDrawRouteService(DrawRouteServiceImpl drawRouteService);

    @Binds public abstract UnitService bindUnitService(UnitServiceImpl unitServiceImpl);

    @Binds public abstract GameService bindGameService(GameServiceImpl gameService);

    @Binds public abstract HealthBarService bindHealthBarService(HealthBarServiceImpl healthBarService);

    @Binds public abstract GooglePlacesNearbySearchService bindGooglePlacesNearbySearchService(GooglePlacesNearbySearchServiceImpl googlePlacesNearbySearchService);

    @Binds public abstract GooglePlacesService bindGooglePlacesService(GooglePlacesServiceImpl googlePlacesService);

    @Binds public abstract NeutralCampService bindNeutralCampService(NeutralCampServiceImpl neutralCampService);

    @Binds public abstract PlayerService bindPlayerService(PlayerServiceImpl playerService);

    @Binds public abstract ProjectionService bindProjectionService(ProjectionServiceImpl projectionService);

    @Binds public abstract GraphicService bindGraphicService(GraphicServiceImpl graphicService);

    @Binds public abstract UnitChoicesMenuService bindUnitChoicesMenuService(UnitChoicesMenuServiceImpl unitChoicesMenuService);

    @Binds public abstract WorldGridService bindWorldGridService(WorldGridServiceImpl worldGridService);

    @Binds public abstract ArtificialOpponentService bindArtificialOpponentService(ArtificialOpponentServiceImpl artificialOpponentService);
}
