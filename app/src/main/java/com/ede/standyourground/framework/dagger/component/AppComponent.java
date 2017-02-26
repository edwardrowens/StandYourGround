package com.ede.standyourground.framework.dagger.component;

import com.ede.standyourground.app.activity.FindMatchActivity;
import com.ede.standyourground.app.activity.MapsActivity;
import com.ede.standyourground.app.service.GoogleDirectionsService;
import com.ede.standyourground.framework.dagger.module.AppModule;
import com.ede.standyourground.framework.dagger.module.ServiceModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, ServiceModule.class})
public interface AppComponent {
    void inject(FindMatchActivity findMatchActivity);
    void inject(MapsActivity mapsActivity);
    void inject(GoogleDirectionsService googleDirectionsService);
}
