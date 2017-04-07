package com.ede.standyourground.game.api.model;

import com.ede.standyourground.R;
import com.ede.standyourground.app.activity.MapsActivity;
import com.google.android.gms.maps.model.CircleOptions;

public enum Units {
    BASE(R.drawable.base, UnitsOptions.CIRCLE_OPTIONS.radius(85), R.color.blue, R.color.red, 0),
    FOOT_SOLDIER(R.drawable.foot_soldier, UnitsOptions.CIRCLE_OPTIONS, R.color.magenta, R.color.red, MapsActivity.resources.getInteger(R.integer.foot_soldier_cost)),
    MARAUDER(R.drawable.marauder, UnitsOptions.CIRCLE_OPTIONS.radius(60), R.color.cyan, R.color.red, MapsActivity.resources.getInteger(R.integer.marauder_cost)),
    MEDIC_NEUTRAL_CAMP(R.drawable.neutral_medic_camp, UnitsOptions.CIRCLE_OPTIONS, R.color.friendlyMedicNeutralCampGreen, R.color.enemyMedicNeutralCampGreen, 0),
    BANK_NEUTRAL_CAMP(R.drawable.neutral_bank_camp, UnitsOptions.CIRCLE_OPTIONS, R.color.friendlyBankNeutralCampGold, R.color.enemyBankNeutralCampGold, 0),
    MEDIC(R.drawable.medic, UnitsOptions.CIRCLE_OPTIONS, R.color.medicPink, R.color.red, MapsActivity.resources.getInteger(R.integer.medic_cost));

    private final int drawableId;
    private final int friendlyColor;
    private final int enemyColor;
    private final CircleOptions circleOptions;
    private final int cost;

    Units(int drawableId, CircleOptions circleOptions, int friendlyColor, int enemyColor, int cost) {
        this.drawableId = drawableId;
        this.circleOptions = circleOptions;
        this.friendlyColor = friendlyColor;
        this.enemyColor = enemyColor;
        this.cost = cost;
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

    public int getCost() {return cost;}
}
