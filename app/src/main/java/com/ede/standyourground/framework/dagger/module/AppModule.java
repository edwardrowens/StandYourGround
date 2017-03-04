package com.ede.standyourground.framework.dagger.module;

import android.content.Context;

import com.ede.standyourground.framework.dagger.providers.GameSessionIdProvider;
import com.ede.standyourground.framework.dagger.providers.GoogleMapProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class AppModule {

    private final Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    public Context context() {
        return context;
    }

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
