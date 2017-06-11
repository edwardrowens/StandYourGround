package com.ede.standyourground.app.ui.impl.component;

import android.app.Activity;
import android.view.ViewGroup;

import com.ede.standyourground.app.ui.api.component.Component;
import com.ede.standyourground.app.ui.api.event.ConfirmRouteListener;
import com.ede.standyourground.app.ui.api.event.ConfirmRouteObserver;
import com.ede.standyourground.app.ui.api.event.RouteCancelListener;
import com.ede.standyourground.app.ui.api.event.RouteCancelObserver;
import com.ede.standyourground.app.ui.api.event.UnitSelectedListener;
import com.ede.standyourground.app.ui.api.event.UnitSelectedObserver;
import com.ede.standyourground.game.api.model.UnitType;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 */

public class UnitChoicesMenuComponent implements Component, ConfirmRouteObserver, UnitSelectedObserver, RouteCancelObserver {

    private final Activity activity;
    private final ViewGroup container;
    private final ViewGroup unitChoices;
    private final ViewGroup unitChoicesMenu;
    private final ViewGroup routeUnitChoice;

    // Listeners
    private final List<ConfirmRouteListener> confirmRouteListeners = new CopyOnWriteArrayList<>();
    private final List<UnitSelectedListener> unitSelectedListeners = new CopyOnWriteArrayList<>();

    private final List<RouteCancelListener> routeCancelListeners = new CopyOnWriteArrayList<>();
    private UnitType selectedUnit;

    public UnitChoicesMenuComponent(Activity activity, ViewGroup unitChoicesMenu, ViewGroup routeUnitChoice, ViewGroup unitChoices) {
        this.activity = activity;
        this.container = unitChoicesMenu;
        this.unitChoices = unitChoices;
        this.unitChoicesMenu = unitChoicesMenu;
        this.routeUnitChoice = routeUnitChoice;
    }

    public ViewGroup getUnitChoices() {
        return unitChoices;
    }

    public ViewGroup getUnitChoicesMenu() {
        return unitChoicesMenu;
    }

    public ViewGroup getRouteUnitChoice() {
        return  routeUnitChoice;
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
    public void registerRouteCancelListener(RouteCancelListener routeCancelListener) {
        routeCancelListeners.add(routeCancelListener);
    }

    @Override
    public void registerConfirmRouteListener(ConfirmRouteListener confirmRouteListener) {
        confirmRouteListeners.add(confirmRouteListener);
    }

    @Override
    public void registerUnitSelectedListener(UnitSelectedListener unitSelectedListener) {
        unitSelectedListeners.add(unitSelectedListener);
    }

    public UnitType getSelectedUnit() {
        return selectedUnit;
    }

    public void setSelectedUnit(UnitType selectedUnit) {
        this.selectedUnit = selectedUnit;
    }

    public List<RouteCancelListener> getRouteCancelListeners() {
        return routeCancelListeners;
    }

    public List<ConfirmRouteListener> getConfirmRouteListeners() {
        return confirmRouteListeners;
    }

    public List<UnitSelectedListener> getUnitSelectedListeners() {
        return unitSelectedListeners;
    }
}
