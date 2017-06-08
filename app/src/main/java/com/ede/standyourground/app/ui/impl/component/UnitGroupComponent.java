package com.ede.standyourground.app.ui.impl.component;

import android.app.Activity;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.ede.standyourground.app.ui.api.component.Component;
import com.ede.standyourground.framework.api.Logger;
import com.google.android.gms.maps.model.LatLng;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public class UnitGroupComponent implements Component {

    private static final Logger logger = new Logger(UnitGroupComponent.class);

    private final Map<UUID, Component> unitGroupBlocks = new ConcurrentHashMap<>();
    private final Activity activity;
    private final ViewGroup container;
    private final LatLng centerPositionReference;
    private final double radiusReference;
    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener;

    public UnitGroupComponent(Activity activity, ViewGroup container, LatLng centerPositionReference, double radiusReference) {
        this.activity = activity;
        this.container = container;
        this.centerPositionReference = centerPositionReference;
        this.radiusReference = radiusReference;
    }

    @Override
    public Activity getActivity() {
        return activity;
    }

    public Map<UUID, Component> getUnitGroupBlocks() {
        return unitGroupBlocks;
    }

    public void addUnitGroupBlock(UUID componentElementId, Component unitGroupBlock) {
        unitGroupBlocks.put(componentElementId, unitGroupBlock);
    }

    @Override
    public ViewGroup getContainer() {
        return container;
    }

    public LatLng getCenterPositionReference() {
        return centerPositionReference;
    }

    public double getRadiusReference() {
        return radiusReference;
    }

    public void setOnGlobalLayoutListener(ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener) {
        this.onGlobalLayoutListener = onGlobalLayoutListener;
    }

    public ViewTreeObserver.OnGlobalLayoutListener getOnGlobalLayoutListener() {
        return onGlobalLayoutListener;
    }
}
