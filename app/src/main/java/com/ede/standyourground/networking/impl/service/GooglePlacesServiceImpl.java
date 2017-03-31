package com.ede.standyourground.networking.impl.service;

import com.ede.standyourground.networking.api.service.GooglePlacesService;

import javax.inject.Inject;

/**
 *
 */

public class GooglePlacesServiceImpl implements GooglePlacesService {

    private static final String GOOGLE_PLACES_PHOTO_URL = "http://192.168.0.103:8000/places/photo/";

    @Inject
    GooglePlacesServiceImpl() {

    }

    @Override
    public String generatePhotoUrl(String photoReference, int maxWidth) {
        return GOOGLE_PLACES_PHOTO_URL + photoReference + "?maxwidth=" + maxWidth;
    }
}
