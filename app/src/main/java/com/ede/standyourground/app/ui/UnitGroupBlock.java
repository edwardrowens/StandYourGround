package com.ede.standyourground.app.ui;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ede.standyourground.R;
import com.ede.standyourground.framework.Logger;
import com.ede.standyourground.framework.dagger.application.MyApp;
import com.ede.standyourground.game.framework.management.impl.WorldManager;
import com.ede.standyourground.game.model.Units;

import java.util.List;
import java.util.UUID;

import dagger.Lazy;

/**
 *
 */

public abstract class UnitGroupBlock implements Component {

    private static final Logger logger = new Logger(UnitGroupBlock.class);

    protected final Activity activity;
    protected final List<UUID> unitIds;
    protected final LinearLayout container;
    protected final RelativeLayout iconContainer;
    private final UUID componentElementId;
    protected Lazy<WorldManager> worldManager;

    public UnitGroupBlock(UUID componentElementId, final List<UUID> unitIds, Activity activity, Units units) {
        worldManager = MyApp.getAppComponent().getWorldManager();
        this.componentElementId = componentElementId;
        this.unitIds = unitIds;
        this.activity = activity;
        this.container = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.unit_group_block, null);
        this.iconContainer = (RelativeLayout) LayoutInflater.from(activity).inflate(R.layout.unit_group_block_icon, null);

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

    protected void clear() {
        clearViews();
        iconContainer.removeAllViews();
        iconContainer.setVisibility(View.GONE);
        container.removeAllViews();
        container.setVisibility(View.GONE);
    }

    public List<UUID> getUnitIds() {
        return unitIds;
    }

    public UUID getComponentElementId() {
        return componentElementId;
    }

    protected abstract void setVisible(int visibility);
    protected abstract void clearViews();
    public abstract View getContainer();
}
