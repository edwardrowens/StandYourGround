package com.ede.standyourground.app.ui.api.component;

import android.app.Activity;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ede.standyourground.R;
import com.ede.standyourground.app.ui.api.service.UnitGroupComponentService;
import com.ede.standyourground.app.ui.impl.component.UnitGroupComponent;
import com.ede.standyourground.framework.api.dagger.providers.GoogleMapProvider;
import com.ede.standyourground.game.api.event.listener.OnDeathListener;
import com.ede.standyourground.game.api.model.Unit;
import com.ede.standyourground.game.api.service.UnitService;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;

import dagger.Lazy;

/**
 *
 */

public class UnitGroupComponentFactory {

    private static final RelativeLayout.LayoutParams CONTAINER_LAYOUT_PARAMS = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    private final Lazy<UnitService> unitService;
    private final Lazy<GoogleMapProvider> googleMapProvider;
    private final Lazy<UnitGroupComponentService> unitGroupComponentService;

    @Inject
    public UnitGroupComponentFactory(Lazy<UnitService> unitService,
                                     Lazy<GoogleMapProvider> googleMapProvider,
                                     Lazy<UnitGroupComponentService> unitGroupComponentService) {
        this.unitService = unitService;
        this.googleMapProvider = googleMapProvider;
        this.unitGroupComponentService = unitGroupComponentService;
    }

    public UnitGroupComponent createUnitGroupComponent(Activity activity, LatLng centerPointReference, double radiusReference) {
        ViewGroup container = (ViewGroup) LayoutInflater.from(activity).inflate(R.layout.unit_group_block_component, (ViewGroup) activity.findViewById(R.id.mapContainer)).findViewById(R.id.unitGroupComponent);

        Projection projection = googleMapProvider.get().getGoogleMap().getProjection();
        Point point = projection.toScreenLocation(centerPointReference);

        RelativeLayout.LayoutParams layoutParams = CONTAINER_LAYOUT_PARAMS;
        layoutParams.leftMargin = point.x;
        layoutParams.topMargin = point.y;
        container.setLayoutParams(layoutParams);

        final UnitGroupComponent unitGroupComponent = new UnitGroupComponent(activity, container, centerPointReference, radiusReference);
        unitGroupComponentService.get().setVisibility(unitGroupComponent, View.VISIBLE);

        unitService.get().registerOnDeathListener(new OnDeathListener() {
            @Override
            public void onDeath(final Unit mortal, Unit killer) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (unitGroupComponent.getUnitGroupBlocks().size() == 0) {
                            unitGroupComponentService.get().clear(unitGroupComponent);
                        }
                    }
                });
            }
        });

        return unitGroupComponent;
    }

    public UnitGroupComponent createUnitGroupComponent(Activity activity, Point point, ViewGroup parent) {
        ViewGroup container = (ViewGroup) LayoutInflater.from(activity).inflate(R.layout.unit_group_block_component, parent).findViewById(R.id.unitGroupComponent);

        RelativeLayout.LayoutParams layoutParams = CONTAINER_LAYOUT_PARAMS;
        layoutParams.leftMargin = point.x;
        layoutParams.topMargin = point.y;
        container.setLayoutParams(layoutParams);

        final UnitGroupComponent unitGroupComponent = new UnitGroupComponent(activity, container, new LatLng(0, 0), 0);
        unitGroupComponentService.get().setVisibility(unitGroupComponent, View.VISIBLE);

        unitService.get().registerOnDeathListener(new OnDeathListener() {
            @Override
            public void onDeath(final Unit mortal, Unit killer) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (unitGroupComponent.getUnitGroupBlocks().size() == 0) {
                            unitGroupComponentService.get().clear(unitGroupComponent);
                        }
                    }
                });
            }
        });

        return unitGroupComponent;
    }
}
