package com.ede.standyourground.networking.impl.service;

import com.ede.standyourground.framework.api.dagger.application.MyApp;
import com.ede.standyourground.networking.api.service.GooglePlacesService;

import javax.inject.Inject;

/**
 *
 */

public class GooglePlacesServiceImpl implements GooglePlacesService {

    private static final String PATH = "places/photo/";
    private final String host;

    @Inject
    GooglePlacesServiceImpl() {
        host = MyApp.getAppComponent().getRetrofit().get().baseUrl().toString();
    }

    @Override
    public String generatePhotoUrl(String photoReference, int maxWidth) {
        return host + PATH + photoReference + "?maxwidth=" + maxWidth;
    }
}
