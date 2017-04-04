package com.ede.standyourground.framework.api.service;

import com.ede.standyourground.game.api.model.Unit;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.Circle;

/**
 *
 */

public interface ProjectionService {
    double calculateUnitPixelRadius(Projection projection, Circle circle);
    double calculateUnitPixelRadius(Projection projection, Unit unit);
}
