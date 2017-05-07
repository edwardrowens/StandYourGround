package com.ede.standyourground.game.api.model;

import com.ede.standyourground.R;
import com.ede.standyourground.app.activity.MapsActivity;

public enum UnitType {
    BASE(R.drawable.base, 85, R.color.blue, R.color.red, 0),
    FOOT_SOLDIER(R.drawable.foot_soldier, 50, R.color.magenta, R.color.red, MapsActivity.resources.getInteger(R.integer.foot_soldier_cost)),
    MARAUDER(R.drawable.marauder, 60, R.color.cyan, R.color.red, MapsActivity.resources.getInteger(R.integer.marauder_cost)),
    MEDIC_NEUTRAL_CAMP(R.drawable.neutral_medic_camp, 50, R.color.friendlyMedicNeutralCampGreen, R.color.enemyMedicNeutralCampGreen, 0),
    BANK_NEUTRAL_CAMP(R.drawable.neutral_bank_camp, 50, R.color.friendlyBankNeutralCampGold, R.color.enemyBankNeutralCampGold, 0),
    MEDIC(R.drawable.medic, 50, R.color.medicPink, R.color.red, MapsActivity.resources.getInteger(R.integer.medic_cost));

    private final int drawableId;
    private final double radius;
    private final int friendlyColor;
    private final int enemyColor;
    private final int cost;

    UnitType(int drawableId, double radius, int friendlyColor, int enemyColor, int cost) {
        this.drawableId = drawableId;
        this.radius = radius;
        this.friendlyColor = friendlyColor;
        this.enemyColor = enemyColor;
        this.cost = cost;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public int getFriendlyColor() {
        return friendlyColor;
    }

    public int getEnemyColor() {
        return enemyColor;
    }

    public int getCost() {return cost;}

    public double getRadius() {
        return radius;
    }
}
