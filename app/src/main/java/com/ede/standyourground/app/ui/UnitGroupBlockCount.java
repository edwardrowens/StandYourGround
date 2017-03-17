package com.ede.standyourground.app.ui;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ede.standyourground.R;
import com.ede.standyourground.app.activity.MapsActivity;
import com.ede.standyourground.framework.Logger;
import com.ede.standyourground.game.model.Unit;
import com.ede.standyourground.game.model.Units;
import com.ede.standyourground.game.model.api.DeathListener;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */

public class UnitGroupBlockCount extends UnitGroupBlock {

    private static Logger logger = new Logger(UnitGroupBlockCount.class);

    private final TextView countContainer;
    private final Activity activity;
    private final AtomicInteger count;

    public UnitGroupBlockCount(UUID componentElementId, final List<UUID> unitIds, Activity activity, Units units, final int count) {
        super(componentElementId, unitIds, activity, units);
        this.count = new AtomicInteger(count);
        this.activity = activity;
        this.countContainer = (TextView) LayoutInflater.from(activity).inflate(R.layout.unit_group_block_count, null);

        countContainer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        countContainer.setText(activity.getResources().getString(R.string.unitGroupCountText, count));
        container.addView(countContainer);
        worldManager.get().registerDeathListener(new DeathListener() {
            @Override
            public void onDeath(Unit mortal) {
                if (unitIds.contains(mortal.getId())) {
                    if (UnitGroupBlockCount.this.count.get() == 1) {
                        for (Unit unit : worldManager.get().getUnits()) {
                            if (!unit.isEnemy() && unit.getType().equals(mortal.getType())) {
                                ViewGroup parent = (ViewGroup) container.getParent();
                                if (parent != null) {
                                    int index = parent.indexOfChild(container);
                                    clear();
                                    parent.removeView(container);
                                    UnitGroupComponent parentComponent = ((UnitGroupComponent) MapsActivity.getComponent(UnitGroupComponent.class));
                                    UnitGroupBlockHealthBar unitGroupBlockHealthBar = parentComponent.createUnitGroupBlockHealthBar(unit.getId(), unit.getType(), unit.getHealth() / (float) unit.getMaxHealth());
                                    parentComponent.addUnitGroupBlockHealthBar(unitGroupBlockHealthBar, index);
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void setVisible(int visibility) {
        countContainer.setVisibility(visibility);
    }

    @Override
    protected void clearViews() {
        countContainer.setVisibility(View.GONE);
    }

    @Override
    public View getContainer() {
        return container;
    }

    @Override
    public Activity getActivity() {
        return activity;
    }

    @Override
    public void drawComponentElements() {
        iconContainer.postInvalidate();
        countContainer.postInvalidate();
    }

    private int decrementCount() {
        count.decrementAndGet();
        countContainer.setText(activity.getResources().getString(R.string.unitGroupCountText, count.get()));
        return count.get();
    }
}
