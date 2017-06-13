package com.ede.standyourground.app.ui.impl.component;

import android.app.Activity;
import android.view.ViewGroup;

import com.ede.standyourground.app.ui.api.component.Component;
import com.ede.standyourground.app.ui.api.event.ComponentChangeListener;
import com.ede.standyourground.app.ui.api.event.ComponentChangeObserver;
import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.game.api.event.listener.OnDeathListener;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 */
public class UnitGroupBlockHealthBarComponent implements Component, ComponentChangeObserver {

    private static final Logger logger = new Logger(UnitGroupBlockHealthBarComponent.class);

    private final ViewGroup container;
    private final UUID componentElementId;
    private final UUID unitId;
    private final Activity activity;
    private final List<ComponentChangeListener> componentChangeListeners = new CopyOnWriteArrayList<>();

    private OnDeathListener onDeathListenerHook;

    public UnitGroupBlockHealthBarComponent(Activity activity, UUID componentElementId, UUID unitId, ViewGroup container) {
        this.container = container;
        this.activity = activity;
        this.componentElementId = componentElementId;
        this.unitId = unitId;
    }

    @Override
    public Activity getActivity() {
        return activity;
    }

    @Override
    public ViewGroup getContainer() {
        return container;
    }

    public UUID getUnitId() {
        return unitId;
    }

    public OnDeathListener getOnDeathListenerHook() {
        return onDeathListenerHook;
    }

    public UUID getComponentElementId() {
        return componentElementId;
    }

    public void setOnDeathListenerHook(OnDeathListener onDeathListenerHook) {
        this.onDeathListenerHook = onDeathListenerHook;
    }

    @Override
    public void registerComponentChangeListener(ComponentChangeListener componentChangeListener) {
        componentChangeListeners.add(componentChangeListener);
    }

    public List<ComponentChangeListener> getComponentChangeListeners() {
        return componentChangeListeners;
    }
}
