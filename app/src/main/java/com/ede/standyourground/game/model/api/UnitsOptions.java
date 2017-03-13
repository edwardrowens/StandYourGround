package com.ede.standyourground.game.model.api;

import android.graphics.Color;

import com.google.android.gms.maps.model.CircleOptions;

/**
 *
 */

public interface UnitsOptions {
    CircleOptions CIRCLE_OPTIONS = new CircleOptions()
            .clickable(true)
            .radius(50)
            .fillColor(0x7F0E003E)
            .strokeColor(Color.BLACK)
            .zIndex(1.0f)
            .strokeWidth(5);
}
