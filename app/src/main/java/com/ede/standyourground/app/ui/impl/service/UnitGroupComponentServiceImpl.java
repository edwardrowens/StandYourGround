package com.ede.standyourground.app.ui.impl.service;

import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;

import com.ede.standyourground.app.ui.api.component.Component;
import com.ede.standyourground.app.ui.api.component.UnitGroupBlockHealthBarComponentFactory;
import com.ede.standyourground.app.ui.api.event.UnitGroupBlockCountComponentChangeListener;
import com.ede.standyourground.app.ui.api.service.UnitGroupBlockCountComponentService;
import com.ede.standyourground.app.ui.api.service.UnitGroupBlockHealthBarComponentService;
import com.ede.standyourground.app.ui.api.service.UnitGroupComponentService;
import com.ede.standyourground.app.ui.impl.component.UnitGroupBlockCount;
import com.ede.standyourground.app.ui.impl.component.UnitGroupBlockHealthBarComponent;
import com.ede.standyourground.app.ui.impl.component.UnitGroupComponent;
import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.framework.api.dagger.providers.GoogleMapProvider;
import com.ede.standyourground.framework.api.service.MathService;
import com.ede.standyourground.framework.api.service.ViewService;
import com.ede.standyourground.game.api.model.Unit;
import com.ede.standyourground.game.api.service.UnitService;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;

import dagger.Lazy;

/**
 *
 */

public class UnitGroupComponentServiceImpl implements UnitGroupComponentService {

    private static final Logger logger = new Logger(UnitGroupComponentServiceImpl.class);

    private final Lazy<ViewService> componentService;
    private final Lazy<GoogleMapProvider> googleMapProvider;
    private final Lazy<MathService> mathService;
    private final Lazy<UnitGroupBlockHealthBarComponentFactory> unitGroupBlockHealthBarComponentFactory;
    private final Lazy<UnitService> unitService;
    private final Lazy<UnitGroupBlockCountComponentService> unitGroupBlockCountComponentService;
    private final Lazy<UnitGroupBlockHealthBarComponentService> unitGroupBlockHealthBarComponentService;

    @Inject
    public UnitGroupComponentServiceImpl(Lazy<ViewService> componentService,
                                         Lazy<GoogleMapProvider> googleMapProvider,
                                         Lazy<MathService> mathService,
                                         Lazy<UnitGroupBlockHealthBarComponentFactory> unitGroupBlockHealthBarComponentFactory,
                                         Lazy<UnitService> unitService,
                                         Lazy<UnitGroupBlockCountComponentService> unitGroupBlockCountComponentService,
                                         Lazy<UnitGroupBlockHealthBarComponentService> unitGroupBlockHealthBarComponentService) {
        this.componentService = componentService;
        this.googleMapProvider = googleMapProvider;
        this.mathService = mathService;
        this.unitGroupBlockHealthBarComponentFactory = unitGroupBlockHealthBarComponentFactory;
        this.unitService = unitService;
        this.unitGroupBlockCountComponentService = unitGroupBlockCountComponentService;
        this.unitGroupBlockHealthBarComponentService = unitGroupBlockHealthBarComponentService;
    }

    @Override
    public void realign(UnitGroupComponent unitGroupComponent, LatLng latLng) {
        removeOnGlobalLayoutListener(unitGroupComponent);
        double radiusReference = unitGroupComponent.getRadiusReference();

        Projection projection = googleMapProvider.get().getGoogleMap().getProjection();
        final Point center = projection.toScreenLocation(latLng);
        Point edge = projection.toScreenLocation(SphericalUtil.computeOffset(latLng, radiusReference, 0d));
        final double lineDistance = mathService.get().calculateLinearDistance(center, edge);
        unitGroupComponent.setOnGlobalLayoutListener(componentService.get().centerViewGroup(unitGroupComponent.getContainer(), center, lineDistance));
    }

    @Override
    public void addUnitGroupBlock(final UnitGroupComponent unitGroupComponent, UnitGroupBlockCount unitGroupBlockCount) {
        if (unitGroupComponent.getContainer().getVisibility() != View.VISIBLE) {
            unitGroupComponent.getContainer().setVisibility(View.VISIBLE);
        }

        unitGroupBlockCount.registerUnitGroupBlockCountComponentChangeListener(new UnitGroupBlockCountComponentChangeListener() {
            @Override
            public void onCountChange(ViewGroup container, Set<UUID> unitIds) {
                if (unitIds.size() == 1) {
                    int index = unitGroupComponent.getContainer().indexOfChild(container);
                    unitGroupComponent.getContainer().removeView(container);
                    UUID unitId = unitIds.iterator().next();
                    Unit unit = unitService.get().getUnit(unitId);
                    if (unit != null) {
                        UnitGroupBlockHealthBarComponent unitGroupBlockHealthBarComponent = unitGroupBlockHealthBarComponentFactory.get().createUnitGroupBlockHealthBar(unitGroupComponent.getActivity(), unitGroupComponent.getContainer(), unit.getId(), unit.getHealth() / unit.getMaxHealth(), unit.getType());
                        addUnitGroupBlock(unitGroupComponent, unitGroupBlockHealthBarComponent, index);
                    }
                }
            }
        });

        unitGroupComponent.getContainer().addView(unitGroupBlockCount.getContainer());
        unitGroupComponent.addUnitGroupBlock(unitGroupBlockCount.getComponentElementId(), unitGroupBlockCount);
    }

    @Override
    public void addUnitGroupBlock(UnitGroupComponent unitGroupComponent, UnitGroupBlockHealthBarComponent unitGroupBlockHealthBarComponent) {
        addUnitGroupBlockHealthBarComponent(unitGroupComponent, unitGroupBlockHealthBarComponent);
    }

    @Override
    public void addUnitGroupBlock(UnitGroupComponent unitGroupComponent, UnitGroupBlockHealthBarComponent unitGroupBlockHealthBarComponent, int index) {
        addUnitGroupBlockHealthBarComponent(unitGroupComponent, unitGroupBlockHealthBarComponent);
    }

    @Override
    public void clear(UnitGroupComponent unitGroupComponent) {
        unitGroupComponent.getContainer().getViewTreeObserver().removeOnGlobalLayoutListener(unitGroupComponent.getOnGlobalLayoutListener());
        setVisibility(unitGroupComponent, View.GONE);
        for (Component component : unitGroupComponent.getUnitGroupBlocks().values()) {
            if (component instanceof UnitGroupBlockCount) {
                unitGroupBlockCountComponentService.get().clear((UnitGroupBlockCount) component);
            } else {
                unitGroupBlockHealthBarComponentService.get().clear((UnitGroupBlockHealthBarComponent) component);
            }
        }
        unitGroupComponent.getUnitGroupBlocks().clear();
        unitGroupComponent.getContainer().removeAllViews();
    }

    @Override
    public void setVisibility(UnitGroupComponent unitGroupComponent, int visibility) {
        unitGroupComponent.getContainer().bringToFront();
        unitGroupComponent.getContainer().setVisibility(visibility);
    }

    private void removeOnGlobalLayoutListener(UnitGroupComponent unitGroupComponent) {
        if (unitGroupComponent.getOnGlobalLayoutListener() != null) {
            unitGroupComponent.getContainer().getViewTreeObserver().removeOnGlobalLayoutListener(unitGroupComponent.getOnGlobalLayoutListener());
            unitGroupComponent.setOnGlobalLayoutListener(null);
        }
    }

    private void addUnitGroupBlockHealthBarComponent(UnitGroupComponent unitGroupComponent, UnitGroupBlockHealthBarComponent unitGroupBlockHealthBarComponent) {
        if (unitGroupComponent.getContainer().getVisibility() != View.VISIBLE) {
            unitGroupComponent.getContainer().setVisibility(View.VISIBLE);
        }
        unitGroupComponent.addUnitGroupBlock(unitGroupBlockHealthBarComponent.getComponentElementId(), unitGroupBlockHealthBarComponent);
    }
}
