package com.ede.standyourground.app.ui;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ede.standyourground.R;
import com.ede.standyourground.game.model.Unit;
import com.ede.standyourground.game.model.Units;
import com.ede.standyourground.game.model.api.DeathListener;

import java.util.List;
import java.util.UUID;

/**
 *
 */

public class UnitGroupBlockCount extends UnitGroupBlock {

    private final TextView countContainer;
    private final Activity activity;
    private int count;

    public UnitGroupBlockCount(UUID componentElementId, final List<UUID> unitIds, Activity activity, Units units, int count) {
        super(componentElementId, unitIds, activity, units);
        this.count = count;
        this.activity = activity;
        this.countContainer = (TextView) LayoutInflater.from(activity).inflate(R.layout.unit_group_block_count, null);

        countContainer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        countContainer.setText(activity.getResources().getString(R.string.unitGroupCountText, count));
        container.addView(countContainer);
        worldManager.get().registerDeathListener(new DeathListener() {
            @Override
            public void onDeath(Unit mortal) {
                if (unitIds.contains(mortal.getId())) {
                    decrementCount();
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
        iconContainer.invalidate();
        countContainer.invalidate();
    }

    private int decrementCount() {
        --count;
        countContainer.setText(activity.getResources().getString(R.string.unitGroupCountText, count));
        return count;
    }
}
