package com.ede.standyourground.game.api.service;

import com.ede.standyourground.game.api.model.UnitType;
import com.ede.standyourground.networking.api.model.GooglePlaceResult;
import com.ede.standyourground.networking.api.model.GooglePlacesType;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 *
 */

public interface NeutralCampService {
    void createNeutralCamps(List<GooglePlaceResult> googlePlaceResults);
    UnitType convertGooglePlacesTypeToNeutralCamp(GooglePlacesType googlePlacesType);
    List<GooglePlaceResult> filterNeutralCamps(List<GooglePlaceResult> googlePlaceResults, LatLng player, LatLng opponent);
}
