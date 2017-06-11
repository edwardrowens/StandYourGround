package com.ede.standyourground.app.ui.impl.service;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ede.standyourground.R;
import com.ede.standyourground.app.ui.api.event.ConfirmRouteListener;
import com.ede.standyourground.app.ui.api.event.RouteCancelListener;
import com.ede.standyourground.app.ui.api.event.UnitSelectedListener;
import com.ede.standyourground.app.ui.api.service.UnitChoicesMenuService;
import com.ede.standyourground.app.ui.impl.component.UnitChoicesMenuComponent;
import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.game.api.model.UnitType;
import com.ede.standyourground.game.api.service.PlayerService;

import javax.inject.Inject;

import dagger.Lazy;

public class UnitChoicesMenuServiceImpl implements UnitChoicesMenuService {

    private static final Logger logger = new Logger(UnitChoicesMenuServiceImpl.class);

    private final Lazy<PlayerService> playerService;

    @Inject
    UnitChoicesMenuServiceImpl(Lazy<PlayerService> playerService) {
        this.playerService = playerService;
    }

    @Override
    public void onUnitSelected(final UnitChoicesMenuComponent unitChoicesMenuComponent, UnitType unitType) {
        ImageView routeUnitChoiceIcon = (ImageView) unitChoicesMenuComponent.getRouteUnitChoice().findViewById(R.id.routeUnitChoiceIcon);
        routeUnitChoiceIcon.setImageDrawable(unitChoicesMenuComponent.getActivity().getDrawable(unitType.getDrawableId()));
        unitChoicesMenuComponent.setSelectedUnit(unitType);
        unitChoicesMenuComponent.getUnitChoices().setVisibility(View.GONE);
        unitChoicesMenuComponent.getRouteUnitChoice().setVisibility(View.VISIBLE);

        for (UnitSelectedListener unitSelectedListener : unitChoicesMenuComponent.getUnitSelectedListeners()) {
            unitSelectedListener.onUnitSelected(unitType);
        }
    }

    @Override
    public void onRouteCancelled(UnitChoicesMenuComponent unitChoicesMenuComponent) {
        unitChoicesMenuComponent.getRouteUnitChoice().setVisibility(View.GONE);
        unitChoicesMenuComponent.getUnitChoices().setVisibility(View.VISIBLE);
        for (RouteCancelListener routeCancelListener : unitChoicesMenuComponent.getRouteCancelListeners()) {
            routeCancelListener.onRouteCancelled();
        }
    }

    @Override
    public void onConfirmRoute(UnitChoicesMenuComponent unitChoicesMenuComponent) {
        unitChoicesMenuComponent.getRouteUnitChoice().setVisibility(View.GONE);
        unitChoicesMenuComponent.getUnitChoices().setVisibility(View.VISIBLE);
        for (ConfirmRouteListener confirmRouteListener : unitChoicesMenuComponent.getConfirmRouteListeners()) {
            confirmRouteListener.onConfirmRoute(unitChoicesMenuComponent.getSelectedUnit());
        }
    }

    @Override
    public void setVisibility(UnitChoicesMenuComponent unitChoicesMenuComponent, UnitType unitType, int visibility) {
        int id;
        switch (unitType) {
            case FOOT_SOLDIER:
                id = R.id.foot_soldier_choice_container;
                break;
            case MARAUDER:
                id = R.id.marauder_choice_container;
                break;
            case MEDIC:
                id = R.id.medic_choice_container;
                break;
            default:
                throw new IllegalArgumentException(String.format("%s does not have a supported unit choice type", unitType.toString()));
        }
        ViewGroup viewGroup = (ViewGroup) unitChoicesMenuComponent.getActivity().findViewById(id);
        viewGroup.setVisibility(visibility);
    }

    @Override
    public void clear(UnitChoicesMenuComponent unitChoicesMenuComponent) {
        unitChoicesMenuComponent.getContainer().setVisibility(View.GONE);
    }

    @Override
    public void checkUnitsAvailableForPurchase(UnitChoicesMenuComponent unitChoicesMenuComponent) {
        ViewGroup unitChoices = unitChoicesMenuComponent.getUnitChoices();

        int medicNeutralCampCount = playerService.get().getMedicNeutralCampCount(playerService.get().getMainPlayerId());
        if (medicNeutralCampCount > 0) {
            setVisibility(unitChoicesMenuComponent, UnitType.MEDIC, View.VISIBLE);
        } else {
            setVisibility(unitChoicesMenuComponent, UnitType.MEDIC, View.GONE);
        }
        if (playerService.get().checkFunds(playerService.get().getMainPlayerId(), UnitType.FOOT_SOLDIER.getCost())) {
            unitChoices.findViewById(R.id.foot_soldier_choice_container).setClickable(true);
        } else {
            unitChoices.findViewById(R.id.foot_soldier_choice_container).setClickable(false);
        }
        if (playerService.get().checkFunds(playerService.get().getMainPlayerId(), UnitType.MARAUDER.getCost())) {
            unitChoices.findViewById(R.id.marauder_choice_container).setClickable(true);
        } else {
            unitChoices.findViewById(R.id.marauder_choice_container).setClickable(false);
        }
        if (playerService.get().checkFunds(playerService.get().getMainPlayerId(), UnitType.MEDIC.getCost())) {
            unitChoices.findViewById(R.id.medic_choice_container).setClickable(true);
        } else {
            unitChoices.findViewById(R.id.medic_choice_container).setClickable(false);
        }
    }
}
