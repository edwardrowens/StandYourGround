package com.ede.standyourground.framework.dagger.module;

import com.ede.standyourground.framework.dagger.providers.GameSessionIdProvider;
import com.ede.standyourground.framework.dagger.providers.GoogleMapProvider;

import javax.inject.Singleton;

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
}
