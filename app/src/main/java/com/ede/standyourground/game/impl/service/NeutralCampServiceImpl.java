package com.ede.standyourground.game.impl.service;

import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.framework.api.service.LatLngService;
import com.ede.standyourground.game.api.model.Hostility;
import com.ede.standyourground.game.api.model.Unit;
import com.ede.standyourground.game.api.model.Units;
import com.ede.standyourground.game.api.service.NeutralCampService;
import com.ede.standyourground.game.api.service.UnitService;
import com.ede.standyourground.networking.api.model.GooglePlaceResult;
import com.ede.standyourground.networking.api.model.GooglePlacesType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;

/**
 *
 */

public class NeutralCampServiceImpl implements NeutralCampService {

    private static final Logger logger = new Logger(NeutralCampServiceImpl.class);

    private static final double CAMPS_PER_KM = 1;
    private static final double RESTRICTING_DISTANCE = 500; // in meters

    private final Lazy<UnitService> unitService;
    private final Lazy<LatLngService> latLngService;

    @Inject
    NeutralCampServiceImpl(Lazy<UnitService> unitService,
                           Lazy<LatLngService> latLngService) {
        this.unitService = unitService;
        this.latLngService = latLngService;
    }

    @Override
    public void createNeutralCamps(List<GooglePlaceResult> googlePlaceResults) {
        for (GooglePlaceResult googlePlaceResult : googlePlaceResults) {
            LatLng position = new LatLng(googlePlaceResult.getGeometry().getLocation().getLat(), googlePlaceResult.getGeometry().getLocation().getLng());
            boolean foundValidType = false;
            for (int i = 0; i < googlePlaceResult.getTypes().size() && !foundValidType; ++i) {
                Units unitType = null;
                try {
                    unitType = convertGooglePlacesTypeToNeutralCamp(GooglePlacesType.valueOf(googlePlaceResult.getTypes().get(i).toUpperCase()));
                } catch (IllegalArgumentException e) {
                    logger.i("%s is not a supported type", googlePlaceResult.getTypes().get(i), e);
                }
                if (unitType != null) {
                    foundValidType = true;
                    String name = googlePlaceResult.getName();
                    String photoReference = null;
                    if (googlePlaceResult.getPhotos() != null && !googlePlaceResult.getPhotos().isEmpty()) {
                        photoReference = googlePlaceResult.getPhotos().get(0).getPhotoReference();
                    }
                    unitService.get().createNeutralUnit(position, unitType, name, photoReference, Hostility.NEUTRAL);
                }
            }
        }
    }

    @Override
    public Units convertGooglePlacesTypeToNeutralCamp(GooglePlacesType googlePlacesType) {
        Units units = null;
        switch (googlePlacesType) {
            case PHARMACY:
                units = Units.MEDIC_NEUTRAL_CAMP;
                break;
            case HOSPITAL:
                units = Units.MEDIC_NEUTRAL_CAMP;
                break;
            case BANK:
                units = Units.BANK_NEUTRAL_CAMP;
                break;
        }

        return units;
    }

    @Override
    public List<GooglePlaceResult> filterNeutralCamps(List<GooglePlaceResult> googlePlaceResults, LatLng player, LatLng opponent) {
        List<GooglePlaceResult> toReturn = new ArrayList<>();
        LatLngBounds latLngBounds = latLngService.get().createBounds(player, opponent);

        List<Unit> units = unitService.get().getUnits();
        for (GooglePlaceResult googlePlaceResult : googlePlaceResults) {
            LatLng position = new LatLng(googlePlaceResult.getGeometry().getLocation().getLat(), googlePlaceResult.getGeometry().getLocation().getLng());
            if (latLngBounds.contains(position)) {
                if (!latLngService.get().withinDistance(player, position, RESTRICTING_DISTANCE) &&
                        !latLngService.get().withinDistance(opponent, position, RESTRICTING_DISTANCE)) {
                    boolean samePosition = false;
                    for (int i = 0; i < units.size() && !samePosition; ++i) {
                        samePosition = units.get(i).getStartingPosition().equals(position);
                    }
                    boolean exceedsDensity = exceedsDensity(position, toReturn);
                    if (!samePosition && !exceedsDensity) {
                        toReturn.add(googlePlaceResult);
                    }
                }
            }
        }
        return toReturn;
    }

    private boolean exceedsDensity(LatLng position, List<GooglePlaceResult> googlePlaceResults) {
        int count = 0;
        for (GooglePlaceResult result : googlePlaceResults) {
            LatLng resultPosition = new LatLng(result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng());
            if (latLngService.get().withinDistance(position, resultPosition, 1000)) {
                ++count;
            }
        }

        return count >= CAMPS_PER_KM;
    }
}
