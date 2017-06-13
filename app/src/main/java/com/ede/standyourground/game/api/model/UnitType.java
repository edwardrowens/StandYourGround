package com.ede.standyourground.game.api.model;

import com.ede.standyourground.R;
import com.ede.standyourground.app.activity.MapsActivity;

public enum UnitType {
    BASE(R.color.baseFriendly, R.drawable.base_friendly, R.drawable.base_unfriendly, 60, 0),
    FOOT_SOLDIER(R.color.footSoldierFriendly, R.drawable.foot_soldier_friendly, R.drawable.foot_soldier_unfriendly, 35, MapsActivity.resources.getInteger(R.integer.foot_soldier_cost)),
    MARAUDER(R.color.marauderFriendly, R.drawable.marauder_friendly, R.drawable.marauder_unfriendly, 40, MapsActivity.resources.getInteger(R.integer.marauder_cost)),
    MEDIC(R.color.medicFriendly, R.drawable.medic_friendly, R.drawable.medic_unfriendly, 35, MapsActivity.resources.getInteger(R.integer.medic_cost)),
    MEDIC_NEUTRAL_CAMP(R.color.medicStationFriendly, R.drawable.medic_station_friendly, R.drawable.medic_station_unfriendly, 50, 0),
    BANK_NEUTRAL_CAMP(R.color.bankFriendly, R.drawable.bank_friendly, R.drawable.bank_unfriendly, 50, 0);

    private final int color;
    private final int friendlyDrawableId;
    private final int unfriendlyDrawableId;
    private final double size;
    private final int cost;

    UnitType(int color, int friendlyDrawableId, int unfriendlyDrawableId, double size, int cost) {
        this.color = color;
        this.friendlyDrawableId = friendlyDrawableId;
        this.unfriendlyDrawableId = unfriendlyDrawableId;
        this.size = size;
        this.cost = cost;
    }

    public int getCost() {return cost;}

    public double getSize() {
        return size;
    }

    public int getUnfriendlyDrawableId() {
        return unfriendlyDrawableId;
    }

    public int getFriendlyDrawableId() {
        return friendlyDrawableId;
    }

    public int getColor() {
        return color;
    }
}
