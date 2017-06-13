package com.ede.standyourground.app.ui.impl.component;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ede.standyourground.app.ui.api.component.Component;
import com.ede.standyourground.app.ui.api.event.ComponentChangeListener;
import com.ede.standyourground.app.ui.api.event.ComponentChangeObserver;
import com.ede.standyourground.game.api.event.listener.OnDeathListener;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 */

public class UnitGroupBlockCount implements Component, ComponentChangeObserver {

    private final TextView countContainer;
    private final ViewGroup container;
    private final Activity activity;
    private final UUID componentElementId;
    private final Set<UUID> unitIds;
    private final List<ComponentChangeListener> componentChangeListeners = new CopyOnWriteArrayList<>();
    private final List<OnDeathListener> onDeathListeners = new CopyOnWriteArrayList<>();

    private OnDeathListener onDeathListenerHook;

    public UnitGroupBlockCount(Activity activity, UUID componentElementId, final Set<UUID> unitIds, ViewGroup container, TextView countContainer) {
        this.activity = activity;
        this.componentElementId = componentElementId;
        this.unitIds = unitIds;
        this.container = container;
        this.countContainer = countContainer;
    }

    @Override
    public Activity getActivity() {
        return activity;
    }

    @Override
    public ViewGroup getContainer() {
        return container;
    }

    @Override
    public void registerComponentChangeListener(ComponentChangeListener componentChangeListener) {
        componentChangeListeners.add(componentChangeListener);
    }

    public TextView getCountContainer() {
        return countContainer;
    }

    public UUID getComponentElementId() {
        return componentElementId;
    }

    public Set<UUID> getUnitIds() {
        return unitIds;
    }

    public List<ComponentChangeListener> getComponentChangeListeners() {
        return componentChangeListeners;
    }

    public OnDeathListener getOnDeathListenerHook() {
        return onDeathListenerHook;
    }

    public void setOnDeathListenerHook(OnDeathListener onDeathListenerHook) {
        this.onDeathListenerHook = onDeathListenerHook;
    }
}
