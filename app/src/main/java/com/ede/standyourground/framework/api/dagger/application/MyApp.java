package com.ede.standyourground.framework.api.dagger.application;

import android.app.Application;

import com.ede.standyourground.framework.api.dagger.component.AppComponent;
import com.ede.standyourground.framework.api.dagger.component.DaggerAppComponent;
import com.ede.standyourground.framework.api.dagger.module.NetModule;

public class MyApp extends Application {

    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .netModule(new NetModule("https://standyourground.herokuapp.com/"))
                .build();
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }
}
