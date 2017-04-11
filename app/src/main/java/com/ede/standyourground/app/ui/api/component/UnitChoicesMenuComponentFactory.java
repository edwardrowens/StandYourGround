package com.ede.standyourground.app.ui.api.component;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.ede.standyourground.R;
import com.ede.standyourground.app.ui.api.service.UnitChoicesMenuService;
import com.ede.standyourground.app.ui.impl.component.UnitChoicesMenuComponent;
import com.ede.standyourground.game.api.model.Units;

import javax.inject.Inject;

import dagger.Lazy;

/**
 *
 */

public class UnitChoicesMenuComponentFactory {

    private final Lazy<UnitChoicesMenuService> unitChoicesMenuService;

    @Inject
    UnitChoicesMenuComponentFactory(Lazy<UnitChoicesMenuService> unitChoicesMenuService) {
        this.unitChoicesMenuService = unitChoicesMenuService;
    }

    public UnitChoicesMenuComponent createUnitChoicesMenuComponent(Activity activity, ViewGroup parent) {
        final ViewGroup unitChoicesMenuParent = (ViewGroup) LayoutInflater.from(activity).inflate(R.layout.unit_choices_menu, parent);
        LinearLayout unitChoicesMenu = (LinearLayout) unitChoicesMenuParent.findViewById(R.id.unitChoicesMenu);
        unitChoicesMenu.setZ(1f);
        HorizontalScrollView unitChoices = (HorizontalScrollView) LayoutInflater.from(activity).inflate(R.layout.unit_choices, unitChoicesMenu).findViewById(R.id.unitChoicesScrollView);
        LinearLayout routeUnitChoice = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.route_unit_choice, unitChoicesMenu).findViewById(R.id.routeUnitChoice);

        final UnitChoicesMenuComponent unitChoicesMenuComponent = new UnitChoicesMenuComponent(activity, unitChoicesMenu, routeUnitChoice, unitChoices);

        unitChoices.findViewById(R.id.medic_choice_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unitChoicesMenuService.get().onUnitSelected(unitChoicesMenuComponent, Units.MEDIC);
            }
        });

        unitChoices.findViewById(R.id.marauder_choice_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unitChoicesMenuService.get().onUnitSelected(unitChoicesMenuComponent, Units.MARAUDER);
            }
        });

        unitChoices.findViewById(R.id.foot_soldier_choice_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unitChoicesMenuService.get().onUnitSelected(unitChoicesMenuComponent, Units.FOOT_SOLDIER);
            }
        });

        routeUnitChoice.findViewById(R.id.routeUnitChoiceCancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unitChoicesMenuService.get().onRouteCancelled(unitChoicesMenuComponent);
            }
        });

        routeUnitChoice.findViewById(R.id.routeUnitChoiceDoneButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unitChoicesMenuService.get().onConfirmRoute(unitChoicesMenuComponent);
            }
        });

        return unitChoicesMenuComponent;
    }
}
