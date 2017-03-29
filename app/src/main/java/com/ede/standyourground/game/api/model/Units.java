package com.ede.standyourground.game.api.model;

import com.ede.standyourground.R;
import com.google.android.gms.maps.model.CircleOptions;

public enum Units {
    BASE(R.drawable.base, UnitsOptions.CIRCLE_OPTIONS.radius(85), R.color.blue, R.color.red),
    FOOT_SOLDIER(R.drawable.foot_soldier, UnitsOptions.CIRCLE_OPTIONS, R.color.magenta, R.color.red),
    MARAUDER(R.drawable.marauder, UnitsOptions.CIRCLE_OPTIONS.radius(60), R.color.cyan, R.color.red),
    MEDIC_NEUTRAL_CAMP(R.drawable.neutral_medic_camp, UnitsOptions.CIRCLE_OPTIONS, R.color.friendlyMedicNeutralCampGreen, R.color.enemyMedicNeutralCampGreen),
    MEDIC(R.drawable.medic, UnitsOptions.CIRCLE_OPTIONS, R.color.medicPink, R.color.red);

    private final int drawableId;
    private final int friendlyColor;
    private final int enemyColor;
    private final CircleOptions circleOptions;

    Units(int drawableId, CircleOptions circleOptions, int friendlyColor, int enemyColor) {
        this.drawableId = drawableId;
        this.circleOptions = circleOptions;
        this.friendlyColor = friendlyColor;
        this.enemyColor = enemyColor;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public CircleOptions getCircleOptions() {return circleOptions;}

    public int getFriendlyColor() {
        return friendlyColor;
    }

    public int getEnemyColor() {
        return enemyColor;
    }
}
