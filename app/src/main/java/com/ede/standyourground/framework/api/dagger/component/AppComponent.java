package com.ede.standyourground.framework.api.dagger.component;

import com.ede.standyourground.app.activity.FindMatchActivity;
import com.ede.standyourground.app.activity.MapsActivity;
import com.ede.standyourground.framework.api.dagger.module.AppModule;
import com.ede.standyourground.framework.api.dagger.module.NetModule;
import com.ede.standyourground.framework.api.dagger.module.ServiceModule;
import com.ede.standyourground.game.api.service.UnitService;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Lazy;
import retrofit2.Retrofit;

@Singleton
@Component(modules = {AppModule.class, ServiceModule.class, NetModule.class})
public interface AppComponent {
    void inject(FindMatchActivity findMatchActivity);
    void inject(MapsActivity mapsActivity);
    Lazy<UnitService> getUnitService();
    Lazy<Retrofit> getRetrofit();
}
