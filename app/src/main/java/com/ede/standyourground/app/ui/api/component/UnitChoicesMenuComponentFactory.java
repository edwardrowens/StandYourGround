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
import com.ede.standyourground.game.api.event.listener.CoinBalanceChangeListener;
import com.ede.standyourground.game.api.model.Player;
import com.ede.standyourground.game.api.model.UnitType;
import com.ede.standyourground.game.api.service.GameService;
import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;

import dagger.Lazy;

/**
 *
 */

public class UnitChoicesMenuComponentFactory {

    private final Lazy<UnitChoicesMenuService> unitChoicesMenuService;
    private final Lazy<GameService> gameService;

    @Inject
    UnitChoicesMenuComponentFactory(Lazy<UnitChoicesMenuService> unitChoicesMenuService,
                                    Lazy<GameService> gameService) {
        this.unitChoicesMenuService = unitChoicesMenuService;
        this.gameService = gameService;
    }

    public UnitChoicesMenuComponent createUnitChoicesMenuComponent(final Activity activity, ViewGroup parent, LatLng centerPoint, double radiusReference) {
        final ViewGroup unitChoicesMenuParent = (ViewGroup) LayoutInflater.from(activity).inflate(R.layout.unit_choices_menu, parent);
        LinearLayout unitChoicesMenu = (LinearLayout) unitChoicesMenuParent.findViewById(R.id.unitChoicesMenu);
        unitChoicesMenu.setZ(1f);
        final HorizontalScrollView unitChoices = (HorizontalScrollView) LayoutInflater.from(activity).inflate(R.layout.unit_choices, unitChoicesMenu).findViewById(R.id.unitChoicesScrollView);
        LinearLayout routeUnitChoice = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.route_unit_choice, unitChoicesMenu).findViewById(R.id.routeUnitChoice);

        final UnitChoicesMenuComponent unitChoicesMenuComponent = new UnitChoicesMenuComponent(activity, unitChoicesMenu, unitChoicesMenu, routeUnitChoice, unitChoices, centerPoint, radiusReference);

        gameService.get().registerCoinBalanceChangeListener(new CoinBalanceChangeListener() {
            @Override
            public void onCoinBalanceChange(final Player player, int oldBalance, int newBalance) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (player.isMainPlayer()) {
                            unitChoicesMenuService.get().checkUnitsAvailableForPurchase(unitChoicesMenuComponent);
                        }
                    }
                });
            }
        });

        unitChoices.findViewById(R.id.medic_choice_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unitChoicesMenuService.get().onUnitSelected(unitChoicesMenuComponent, UnitType.MEDIC);
            }
        });

        unitChoices.findViewById(R.id.marauder_choice_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unitChoicesMenuService.get().onUnitSelected(unitChoicesMenuComponent, UnitType.MARAUDER);
            }
        });

        unitChoices.findViewById(R.id.foot_soldier_choice_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unitChoicesMenuService.get().onUnitSelected(unitChoicesMenuComponent, UnitType.FOOT_SOLDIER);
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
