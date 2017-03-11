package com.ede.standyourground.app.ui;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ede.standyourground.R;
import com.ede.standyourground.game.model.Units;

import java.util.UUID;

/**
 *
 */

public abstract class UnitGroupBlock implements Component {

    protected final Activity activity;
    protected final UUID componentElementId;
    protected final LinearLayout container;
    protected final RelativeLayout iconContainer;

    public UnitGroupBlock(UUID componentElementId, Activity activity, Units units) {
        this.componentElementId = componentElementId;
        this.activity = activity;
        this.container = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.unit_group_block, null);
        this.iconContainer = (RelativeLayout) LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.unit_group_block_icon, null);

        Icon icon = new Icon(componentElementId, activity, units);

        container.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        iconContainer.setLayoutParams(new ViewGroup.LayoutParams(icon.getIcon().getIntrinsicWidth(), icon.getIcon().getIntrinsicHeight()));

        iconContainer.addView(icon.getContainer());

        container.addView(iconContainer);
    }

    public void setVisibility(int visibility) {
        setVisible(visibility);
        iconContainer.setVisibility(visibility);
    }

    protected abstract void setVisible(int visibility);
    public abstract View getContainer();
    public abstract UUID getComponentElementId();
}
