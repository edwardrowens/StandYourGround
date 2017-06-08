package com.ede.standyourground.app.ui.impl.component;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.ede.standyourground.app.ui.api.component.Component;
import com.ede.standyourground.app.ui.api.event.ConfirmRouteListener;
import com.ede.standyourground.app.ui.api.event.ConfirmRouteObserver;
import com.ede.standyourground.app.ui.api.event.RouteCancelListener;
import com.ede.standyourground.app.ui.api.event.RouteCancelObserver;
import com.ede.standyourground.app.ui.api.event.UnitSelectedListener;
import com.ede.standyourground.app.ui.api.event.UnitSelectedObserver;
import com.ede.standyourground.game.api.model.UnitType;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 */

public class UnitChoicesMenuComponent implements Component, ConfirmRouteObserver, UnitSelectedObserver, RouteCancelObserver {

    private final Activity activity;
    private final ViewGroup container;
    private final HorizontalScrollView unitChoices;
    private final LinearLayout unitChoicesMenu;
    private final LinearLayout routeUnitChoice;
    private final LatLng centerPointReference;
    private final double radiusReference;

    // Listeners
    private final List<ConfirmRouteListener> confirmRouteListeners = new CopyOnWriteArrayList<>();
    private final List<UnitSelectedListener> unitSelectedListeners = new CopyOnWriteArrayList<>();

    private final List<RouteCancelListener> routeCancelListeners = new CopyOnWriteArrayList<>();
    private UnitType selectedUnit;

    public UnitChoicesMenuComponent(Activity activity, ViewGroup container, LinearLayout unitChoicesMenu, LinearLayout routeUnitChoice, HorizontalScrollView unitChoices, LatLng centerPointReference, double radiusReference) {
        this.activity = activity;
        this.container = container;
        this.unitChoices = unitChoices;
        this.unitChoicesMenu = unitChoicesMenu;
        this.routeUnitChoice = routeUnitChoice;
        this.centerPointReference = centerPointReference;
        this.radiusReference = radiusReference;
    }

    public HorizontalScrollView getUnitChoices() {
        return unitChoices;
    }

    public LinearLayout getUnitChoicesMenu() {
        return unitChoicesMenu;
    }

    public LinearLayout getRouteUnitChoice() {
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

    public LatLng getCenterPointReference() {
        return centerPointReference;
    }

    public double getRadiusReference() {
        return radiusReference;
    }
}
