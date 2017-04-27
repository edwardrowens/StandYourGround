package com.ede.standyourground.framework.api.dagger.component;

import com.ede.standyourground.app.activity.FindMatchActivity;
import com.ede.standyourground.app.activity.MainActivity;
import com.ede.standyourground.app.activity.MapsActivity;
import com.ede.standyourground.app.activity.SelectLocationActivity;
import com.ede.standyourground.app.ui.impl.component.NeutralCampListingComponent;
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
    void inject(NeutralCampListingComponent neutralCampListingComponent);
    void inject(SelectLocationActivity selectLocationActivity);
    void inject(MainActivity mainActivity);
    Lazy<UnitService> getUnitService();
    Lazy<Retrofit> getRetrofit();
}
