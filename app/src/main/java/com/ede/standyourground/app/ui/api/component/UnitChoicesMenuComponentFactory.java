package com.ede.standyourground.app.ui.api.component;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.ede.standyourground.R;
import com.ede.standyourground.app.ui.api.service.UnitChoicesMenuService;
import com.ede.standyourground.app.ui.impl.component.UnitChoicesMenuComponent;
import com.ede.standyourground.game.api.event.listener.CoinBalanceChangeListener;
import com.ede.standyourground.game.api.model.Player;
import com.ede.standyourground.game.api.model.UnitType;
import com.ede.standyourground.game.api.service.GameService;

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

    public UnitChoicesMenuComponent createUnitChoicesMenuComponent(final Activity activity, ViewGroup parent) {
        final ViewGroup unitChoicesMenu = (ViewGroup) parent.findViewById(R.id.unitChoicesMenu);
        final ViewGroup unitChoices = (ViewGroup) unitChoicesMenu.findViewById(R.id.unitChoicesScrollView);
        final ViewGroup routeUnitChoice = (ViewGroup) unitChoicesMenu.findViewById(R.id.routeUnitChoice);

        final UnitChoicesMenuComponent unitChoicesMenuComponent = new UnitChoicesMenuComponent(activity, unitChoicesMenu, routeUnitChoice, unitChoices);

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
