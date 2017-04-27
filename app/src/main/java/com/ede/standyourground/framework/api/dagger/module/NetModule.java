package com.ede.standyourground.framework.api.dagger.module;

import com.ede.standyourground.framework.Lazy;
import com.ede.standyourground.framework.api.dagger.providers.GameModeProvider;
import com.ede.standyourground.networking.api.NetworkingHandler;
import com.ede.standyourground.networking.impl.MockNetworkingHandler;
import com.ede.standyourground.networking.impl.NetworkingHandlerImpl;
import com.ede.standyourground.networking.impl.exchange.handler.request.CreateUnitRequestHandler;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetModule {
    String baseUrl;

    public NetModule(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new Gson();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder().build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    NetworkingHandler provideNetworkingHandler(GameModeProvider gameModeProvider, CreateUnitRequestHandler createUnitRequestHandler, Gson gson, Retrofit retrofit) {
        switch (gameModeProvider.getGameMode()) {
            case SINGLE_PLAYER:
                return new MockNetworkingHandler();
            case MULTIPLAYER:
                return new NetworkingHandlerImpl(Lazy.of(createUnitRequestHandler), Lazy.of(gson), Lazy.of(retrofit));
            default:
                throw new UnsupportedOperationException("Invalid game mode selected");
        }
    }
}
