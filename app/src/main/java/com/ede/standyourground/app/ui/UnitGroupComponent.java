package com.ede.standyourground.app.ui;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.RelativeLayout;

import com.ede.standyourground.R;
import com.ede.standyourground.framework.Logger;
import com.ede.standyourground.framework.dagger.application.MyApp;
import com.ede.standyourground.game.model.Unit;
import com.ede.standyourground.game.model.Units;
import com.ede.standyourground.game.model.api.OnDeathListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public class UnitGroupComponent implements Component {

    private static final Logger logger = new Logger(UnitGroupComponent.class);

    private static final RelativeLayout.LayoutParams CONTAINER_LAYOUT_PARAMS = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    private final Map<UUID, UnitGroupBlock> unitGroupBlocks = new ConcurrentHashMap<>();
    private final Set<UUID> unitIds = new HashSet<>();
    private final Activity activity;
    private final GridLayout gridLayout;

    public UnitGroupComponent(Activity activity, Point point) {
        this.activity = activity;
        this.gridLayout = (GridLayout) LayoutInflater.from(activity).inflate(R.layout.unit_group_block_component, null);

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

        MyApp.getAppComponent().getUnitService().get().registerOnDeathListener(new OnDeathListener() {
            @Override
            public void onDeath(final Unit mortal) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (unitIds.remove(mortal.getId())) {
                            if (unitIds.size() == 0) {
                                clear();
                            }
                        }
                    }
                });
            }
        });

        setVisibility(View.INVISIBLE);
    }

    public UUID createAndAddUnitGroupBlockHealthBar(final UUID unitId, Units units, float healthPercentage) {
        UnitGroupBlockHealthBar unitGroupBlockHealthBar = createUnitGroupBlockHealthBar(unitId, units, healthPercentage);
        return addUnitGroupBlockHealthBar(unitGroupBlockHealthBar, null);
    }

    public UnitGroupBlockHealthBar createUnitGroupBlockHealthBar(final UUID unitId, Units units, float healthPercentage) {
        HealthBar healthBar = new HealthBar(unitId, activity);
        healthBar.setHealthPercentage(healthPercentage);
        healthBar.setWidth(100);
        healthBar.setHeight(50);
        PointF pointF = new PointF();
        pointF.x = 0f;
        pointF.y = 0f;
        healthBar.setPoint(pointF);

        List<UUID> unitIds = new ArrayList<>();
        unitIds.add(unitId);

        return new UnitGroupBlockHealthBar(UUID.randomUUID(), unitIds, activity, units, healthBar);
    }

    public UUID addUnitGroupBlockHealthBar(UnitGroupBlockHealthBar unitGroupBlockHealthBar, Integer index) {
        if (gridLayout.getVisibility() != View.VISIBLE) {
            setVisibility(View.VISIBLE);
        }
        unitIds.add(unitGroupBlockHealthBar.getUnitIds().get(0));
        unitGroupBlocks.put(unitGroupBlockHealthBar.getComponentElementId(), unitGroupBlockHealthBar);
        if (index == null) {
            gridLayout.addView(unitGroupBlockHealthBar.getContainer());
        } else {
            gridLayout.addView(unitGroupBlockHealthBar.getContainer(), index);
        }

        return unitGroupBlockHealthBar.getComponentElementId();
    }

    public UUID createUnitGroupBlockCount(List<UUID> unitIds, Units units, int count) {
        if (gridLayout.getVisibility() != View.VISIBLE) {
            setVisibility(View.VISIBLE);
        }
        this.unitIds.addAll(unitIds);
        UUID unitGroupBlockId = UUID.randomUUID();
        final UnitGroupBlockCount unitGroupBlockCount = new UnitGroupBlockCount(unitGroupBlockId, unitIds, activity, units, count);

        unitGroupBlockCount.registerFinalDecrementListener(new FinalDecrementListener() {
            @Override
            public void onFinalDecrement(final Unit unit) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int index = gridLayout.indexOfChild(unitGroupBlockCount.getContainer());
                        clear();
                        gridLayout.removeView(unitGroupBlockCount.getContainer());
                        UnitGroupBlockHealthBar unitGroupBlockHealthBar = createUnitGroupBlockHealthBar(unit.getId(), unit.getType(), unit.getHealth() / (float) unit.getMaxHealth());
                        addUnitGroupBlockHealthBar(unitGroupBlockHealthBar, index);
                    }
                });
            }
        });

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
        gridLayout.postInvalidate();
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
        if (unitGroupBlocks.size() == 0) {
            setVisibility(View.GONE);
        }
    }
}
