package com.ede.standyourground.framework.dagger.module;

import com.ede.standyourground.framework.dagger.providers.GameSessionIdProvider;
import com.ede.standyourground.framework.dagger.providers.GoogleMapProvider;
import com.ede.standyourground.networking.exchange.handler.impl.request.CreateUnitRequestHandler;
import com.ede.standyourground.networking.framework.NetworkingManager;

import javax.inject.Singleton;

import dagger.Lazy;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class AppModule {

    @Provides
    @Singleton
    static GoogleMapProvider googleMapProvider() {
        return new GoogleMapProvider();
    }

    @Provides
    @Singleton
    static GameSessionIdProvider gameSessionIdProvider() {
        return new GameSessionIdProvider();
    }

    @Provides
    @Singleton
    static NetworkingManager provideNetworkingManager(Lazy<CreateUnitRequestHandler> createUnitRequestHandler) {
        return new NetworkingManager(createUnitRequestHandler);
    }

}
