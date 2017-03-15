package com.ede.standyourground.framework.dagger.application;

import android.app.Application;

import com.ede.standyourground.framework.dagger.component.AppComponent;
import com.ede.standyourground.framework.dagger.component.DaggerAppComponent;

public class MyApp extends Application {

    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.create();
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }
}
