package com.ede.standyourground.game.model;

import com.ede.standyourground.R;
import com.ede.standyourground.game.model.api.UnitsOptions;
import com.google.android.gms.maps.model.CircleOptions;

public enum Units {
    BASE(R.drawable.base, UnitsOptions.CIRCLE_OPTIONS.radius(75), R.color.blue, R.color.red),
    FOOT_SOLDIER(R.drawable.foot_soldier, UnitsOptions.CIRCLE_OPTIONS, R.color.magenta, R.color.red),
    MARAUDER(R.drawable.marauder, UnitsOptions.CIRCLE_OPTIONS.radius(60), R.color.cyan, R.color.red);

    private final int drawableId;
    private final int playerColor;
    private final int enemyColor;
    private final CircleOptions circleOptions;

    Units(int drawableId, CircleOptions circleOptions, int playerColor, int enemyColor) {
        this.drawableId = drawableId;
        this.circleOptions = circleOptions;
        this.playerColor = playerColor;
        this.enemyColor = enemyColor;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public CircleOptions getCircleOptions() {return circleOptions;}

    public int getPlayerColor() {
        return playerColor;
    }

    public int getEnemyColor() {
        return enemyColor;
    }
}
