package com.ede.standyourground.game.model;

import com.ede.standyourground.R;
import com.ede.standyourground.game.model.api.UnitsOptions;
import com.google.android.gms.maps.model.CircleOptions;

public enum Units {
    BASE(R.drawable.base, UnitsOptions.CIRCLE_OPTIONS.radius(75)),
    FOOT_SOLDIER(R.drawable.base, UnitsOptions.CIRCLE_OPTIONS),
    MARAUDER(R.drawable.base, UnitsOptions.CIRCLE_OPTIONS.radius(60));

    private final int drawableId;
    private final CircleOptions circleOptions;

    Units(int drawableId, CircleOptions circleOptions) {
        this.drawableId = drawableId;
        this.circleOptions = circleOptions;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public CircleOptions getCircleOptions() {return circleOptions;}
}
