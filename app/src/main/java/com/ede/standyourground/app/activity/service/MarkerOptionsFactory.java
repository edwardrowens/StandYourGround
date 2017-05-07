package com.ede.standyourground.app.activity.service;

import com.ede.standyourground.framework.api.service.GraphicService;
import com.ede.standyourground.game.api.model.Unit;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

import dagger.Lazy;

/**
 *
 */

public class MarkerOptionsFactory {

    private final Lazy<GraphicService> graphicService;

    @Inject
    public MarkerOptionsFactory(Lazy<GraphicService> graphicService) {
        this.graphicService = graphicService;
    }

    public MarkerOptions createMarkerOptions(Unit unit, String hexColor) {
        return new MarkerOptions().position(unit.getStartingPosition()).icon(BitmapDescriptorFactory.defaultMarker(graphicService.get().hexToHue(hexColor))).zIndex(1.0f);
    }
}
