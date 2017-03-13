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
import com.ede.standyourground.framework.dagger.application.MyApp;
import com.ede.standyourground.game.framework.management.impl.WorldManager;
import com.ede.standyourground.game.model.Unit;
import com.ede.standyourground.game.model.Units;
import com.ede.standyourground.game.model.api.DeathListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import dagger.Lazy;

/**
 *
 */
public class UnitGroupComponent implements Component {

    private static final RelativeLayout.LayoutParams CONTAINER_LAYOUT_PARAMS = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    private final Map<UUID, UnitGroupBlock> unitGroupBlocks = new ConcurrentHashMap<>();
    private final List<UUID> unitIds = new ArrayList<>();
    private final Activity activity;
    private final GridLayout gridLayout;
    Lazy<WorldManager> worldManager;

    public UnitGroupComponent(Activity activity, Point point) {

        ((MyApp) activity.getApplication()).getAppComponent().inject(this);
        this.activity = activity;
        this.gridLayout = (GridLayout) LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.unit_group_block_component, null);

        RelativeLayout.LayoutParams layoutParams = CONTAINER_LAYOUT_PARAMS;
        layoutParams.leftMargin = point.x;
        layoutParams.topMargin = point.y;
        this.gridLayout.setLayoutParams(layoutParams);
        gridLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });

        worldManager.get().registerDeathListener(new DeathListener() {
            @Override
            public void onDeath(Unit mortal) {
                if (unitIds.remove(mortal.getId())) {
                    if (unitIds.size() == 0) {
                        clear();
                    }
                }
            }
        });
        setVisibility(View.INVISIBLE);
    }

    @Inject
    void setWorldManager(Lazy<WorldManager> worldManager) {
        this.worldManager = worldManager;
    }

    public void createUnitGroupBlockHealthBar(UUID unitId, Units units, float healthPercentage) {
        if (gridLayout.getVisibility() != View.VISIBLE) {
            setVisibility(View.VISIBLE);
        }
        unitIds.add(unitId);

        HealthBar healthBar = new HealthBar(unitId, activity.getApplicationContext());
        healthBar.setHealthPercentage(healthPercentage);
        healthBar.setWidth(100);
        healthBar.setHeight(50);
        PointF pointF = new PointF();
        pointF.x = 0f;
        pointF.y = 0f;
        healthBar.setPoint(pointF);

        List<UUID> unitIds = new ArrayList<>();
        unitIds.add(unitId);

        UUID unitGroupBlockId = UUID.randomUUID();

        UnitGroupBlockHealthBar unitGroupBlockHealthBar = new UnitGroupBlockHealthBar(unitGroupBlockId, unitIds, activity, units, healthBar);
        gridLayout.addView(unitGroupBlockHealthBar.getContainer());
        unitGroupBlocks.put(unitGroupBlockId, unitGroupBlockHealthBar);
    }

    public UUID createUnitGroupBlockCount(List<UUID> unitIds, Units units, int count) {
        if (gridLayout.getVisibility() != View.VISIBLE) {
            setVisibility(View.VISIBLE);
        }
        unitIds.addAll(unitIds);
        UUID unitGroupBlockId = UUID.randomUUID();
        UnitGroupBlockCount unitGroupBlockCount = new UnitGroupBlockCount(unitGroupBlockId, unitIds, activity, units, count);
        gridLayout.addView(unitGroupBlockCount.getContainer());
        unitGroupBlocks.put(unitGroupBlockId, unitGroupBlockCount);

        return unitGroupBlockId;
    }

    public void removeComponentElement(UUID componentElementId) {
        removeView(componentElementId);
    }

    public UnitGroupBlock getElement(UUID componentElementId) {
        return unitGroupBlocks.get(componentElementId);
    }

    public void clear() {
        for (UnitGroupBlock block : unitGroupBlocks.values()) {
            removeView(block.getComponentElementId());
        }
        unitGroupBlocks.clear();
        unitIds.clear();
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

    public void initialize(int containerId) {
        ((RelativeLayout) activity.findViewById(containerId)).addView(gridLayout);
    }

    private void setVisibility(int visibility) {
        gridLayout.setVisibility(visibility);
        for (UnitGroupBlock unitGroupBlock : unitGroupBlocks.values()) {
            unitGroupBlock.setVisibility(visibility);
        }
    }

    private void removeView(UUID componentElementId) {
        unitGroupBlocks.get(componentElementId).clear();
        unitIds.removeAll(unitGroupBlocks.get(componentElementId).getUnitIds());
        gridLayout.removeView(unitGroupBlocks.get(componentElementId).getContainer());
        unitGroupBlocks.remove(componentElementId);
        if (unitGroupBlocks.size() == 0 ) {
            setVisibility(View.INVISIBLE);
        }
    }
}
