package com.ede.standyourground.app.ui;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ede.standyourground.R;
import com.ede.standyourground.game.model.Units;

import java.util.UUID;

/**
 *
 */

public class UnitGroupBlockCount extends UnitGroupBlock {

    private final TextView countContainer;
    private int count;

    public UnitGroupBlockCount(UUID componentElementId, Activity activity, Units units, int count) {
        super(componentElementId, activity, units);
        this.count = count;
        this.countContainer = (TextView) LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.unit_group_block_count, null);

        countContainer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        countContainer.setText(activity.getResources().getString(R.string.unitGroupCountText, count));
        container.addView(countContainer);
    }

    @Override
    protected void setVisible(int visibility) {
        countContainer.setVisibility(visibility);
    }

    @Override
    public View getContainer() {
        return container;
    }

    @Override
    public UUID getComponentElementId() {
        return componentElementId;
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

    public int decrementCount() {
        return --count;
    }
}
