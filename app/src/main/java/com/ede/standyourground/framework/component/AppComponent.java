package com.ede.standyourground.framework.component;

import com.ede.standyourground.app.activity.FindMatchActivity;
import com.ede.standyourground.app.activity.MapsActivity;
import com.ede.standyourground.framework.modules.AppModule;
import com.ede.standyourground.framework.modules.ServiceModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, ServiceModule.class})
public interface AppComponent {
    void inject(FindMatchActivity findMatchActivity);
    void inject(MapsActivity mapsActivity);
}
