package com.ede.standyourground.networking.api.event;

import com.ede.standyourground.networking.api.model.GooglePlaceResult;

import java.util.List;

/**
 *
 */

public interface GooglePlacesResponseCallback {
    void onResponse(List<GooglePlaceResult> results, int statusCode);
    void onFailure(Throwable t);
}
