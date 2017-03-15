package com.ede.standyourground.framework.dagger.component;

import com.ede.standyourground.app.activity.FindMatchActivity;
import com.ede.standyourground.app.activity.MapsActivity;
import com.ede.standyourground.framework.dagger.module.AppModule;
import com.ede.standyourground.framework.dagger.module.ServiceModule;
import com.ede.standyourground.game.framework.management.impl.WorldManager;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Lazy;

@Singleton
@Component(modules = {AppModule.class, ServiceModule.class})
public interface AppComponent {
    void inject(FindMatchActivity findMatchActivity);
    void inject(MapsActivity mapsActivity);
    Lazy<WorldManager> getWorldManager();
}
