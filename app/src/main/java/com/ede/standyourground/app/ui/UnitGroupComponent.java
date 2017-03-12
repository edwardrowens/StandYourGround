package com.ede.standyourground.app.ui;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.PointF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.RelativeLayout;

import com.ede.standyourground.R;
import com.ede.standyourground.game.model.Units;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public class UnitGroupComponent implements Component {

    private static final RelativeLayout.LayoutParams CONTAINER_LAYOUT_PARAMS = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    private final Map<UUID, UnitGroupBlock> unitGroupBlocks = new ConcurrentHashMap<>();
    private final Activity activity;
    private final GridLayout gridLayout;

    public UnitGroupComponent(Activity activity, Point point) {
        this.activity = activity;
        this.gridLayout = (GridLayout) LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.unit_group_block_component, null);

        RelativeLayout.LayoutParams layoutParams = CONTAINER_LAYOUT_PARAMS;
        layoutParams.leftMargin = point.x;
        layoutParams.topMargin = point.y;
        this.gridLayout.setLayoutParams(layoutParams);
        setVisibility(View.INVISIBLE);
    }

    public void createUnitGroupBlockHealthBar(UUID healthBarId, Units units, float healthPercentage) {
        if (gridLayout.getVisibility() != View.VISIBLE) {
            setVisibility(View.VISIBLE);
        }
        HealthBar healthBar = new HealthBar(healthBarId, activity.getApplicationContext());
        healthBar.setHealthPercentage(healthPercentage);
        healthBar.setWidth(100);
        healthBar.setHeight(50);
        PointF pointF = new PointF();
        pointF.x = 0f;
        pointF.y = 0f;
        healthBar.setPoint(pointF);

        UnitGroupBlockHealthBar unitGroupBlockHealthBar = new UnitGroupBlockHealthBar(healthBarId, activity, units, healthBar);
        gridLayout.addView(unitGroupBlockHealthBar.getContainer());
        unitGroupBlocks.put(healthBarId, unitGroupBlockHealthBar);
    }

    public UUID createUnitGroupBlockCount(Units units, int count) {
        if (gridLayout.getVisibility() != View.VISIBLE) {
            setVisibility(View.VISIBLE);
        }
        UUID unitGroupBlockId = UUID.randomUUID();
        UnitGroupBlockCount unitGroupBlockCount = new UnitGroupBlockCount(unitGroupBlockId, activity, units, count);
        gridLayout.addView(unitGroupBlockCount.getContainer());
        unitGroupBlocks.put(unitGroupBlockId, unitGroupBlockCount);

        return unitGroupBlockId;
    }

    public void removeComponentElement(UUID componentElementId) {
        gridLayout.removeView(unitGroupBlocks.get(componentElementId).getContainer());
        unitGroupBlocks.remove(componentElementId);
        if (unitGroupBlocks.size() == 0 ) {
            setVisibility(View.INVISIBLE);
        }
    }

    public UnitGroupBlock getElement(UUID componentElementId) {
        return unitGroupBlocks.get(componentElementId);
    }

    public void clear() {
        for (UnitGroupBlock block : unitGroupBlocks.values()) {
            gridLayout.removeView(block.getContainer());
        }
        unitGroupBlocks.clear();
    }

    public boolean isVisible() {
        return unitGroupBlocks.size() == 0;
    }

    public void setPoint(Point point) {
        RelativeLayout.LayoutParams layoutParams = CONTAINER_LAYOUT_PARAMS;
        layoutParams.leftMargin = point.x;
        layoutParams.topMargin = point.y;
        this.gridLayout.setLayoutParams(layoutParams);
    }

    @Override
    public Activity getActivity() {
        return activity;
    }

    @Override
    public void drawComponentElements() {
        gridLayout.invalidate();
        for (UnitGroupBlock unitGroupBlock : unitGroupBlocks.values()) {
            unitGroupBlock.drawComponentElements();
        }
    }

    public void initialize() {
        ((RelativeLayout) activity.findViewById(R.id.mapContainer)).addView(gridLayout);
    }

    private void setVisibility(int visibility) {
        gridLayout.setVisibility(visibility);
        for (UnitGroupBlock unitGroupBlock : unitGroupBlocks.values()) {
            unitGroupBlock.setVisibility(visibility);
        }
    }
}
