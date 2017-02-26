package com.ede.standyourground.framework.modules;

import com.ede.standyourground.framework.providers.GoogleMapProvider;
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
    GoogleMapProvider googleMapProvider() {
        return new GoogleMapProvider();
    }

    @Provides
    @Singleton
    NetworkingManager provideNetworkingManager(Lazy<CreateUnitRequestHandler> createUnitRequestHandler) {
        return new NetworkingManager(createUnitRequestHandler);
    }

}
