package com.ede.standyourground.app.ui;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ede.standyourground.R;
import com.ede.standyourground.framework.Logger;
import com.ede.standyourground.game.model.Units;

import java.util.List;
import java.util.UUID;

/**
 *
 */
public class UnitGroupBlockHealthBar extends UnitGroupBlock {

    private static final Logger logger = new Logger(UnitGroupBlockHealthBar.class);

    private final RelativeLayout healthBarContainer;

    public UnitGroupBlockHealthBar(UUID componentElementId, List<UUID> unitIds, Activity activity, Units units, HealthBar healthBar) {
        super(componentElementId, unitIds, activity, units);
        this.healthBarContainer = (RelativeLayout) LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.unit_group_block_health_bar, null);

        healthBarContainer.setLayoutParams(new ViewGroup.LayoutParams((int)healthBar.getHealthBarBorder().width(), (int)healthBar.getHealthBarBorder().height()));
        this.healthBarContainer.addView(healthBar);
        this.container.addView(healthBarContainer);
    }

    @Override
    public Activity getActivity() {
        return activity;
    }

    @Override
    public void drawComponentElements() {
        this.iconContainer.invalidate();
        this.healthBarContainer.invalidate();
        this.container.invalidate();
    }

    @Override
    protected void setVisible(int visibility) {
        healthBarContainer.setVisibility(visibility);
    }

    @Override
    protected void clearViews() {
        healthBarContainer.removeAllViews();
    }

    public LinearLayout getContainer() {
        return container;
    }
}
