package com.ede.standyourground.framework.dagger.application;

import android.app.Application;

import com.ede.standyourground.framework.dagger.component.AppComponent;
import com.ede.standyourground.framework.dagger.component.DaggerAppComponent;

public class MyApp extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.create();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
