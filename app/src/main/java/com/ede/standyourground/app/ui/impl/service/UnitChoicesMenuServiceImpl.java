package com.ede.standyourground.app.ui.impl.service;

import android.graphics.Point;
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
import com.ede.standyourground.game.api.model.Units;
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
    public void onUnitSelected(final UnitChoicesMenuComponent unitChoicesMenuComponent, Units units) {
        unitChoicesMenuComponent.getUnitChoices().setVisibility(View.GONE);
        ImageView routeUnitChoiceIcon = (ImageView) unitChoicesMenuComponent.getRouteUnitChoice().findViewById(R.id.routeUnitChoiceIcon);
        routeUnitChoiceIcon.setImageDrawable(unitChoicesMenuComponent.getActivity().getDrawable(units.getDrawableId()));
        unitChoicesMenuComponent.getRouteUnitChoice().setVisibility(View.VISIBLE);
        unitChoicesMenuComponent.setSelectedUnit(units);

        for (UnitSelectedListener unitSelectedListener : unitChoicesMenuComponent.getUnitSelectedListeners()) {
            unitSelectedListener.onUnitSelected(units);
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
    public void setVisibility(UnitChoicesMenuComponent unitChoicesMenuComponent, Units units, int visibility) {
        int id;
        switch (units) {
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
                throw new IllegalArgumentException(String.format("%s does not have a supported unit choice type", units.toString()));
        }
        ViewGroup viewGroup = (ViewGroup) unitChoicesMenuComponent.getActivity().findViewById(id);
        viewGroup.setVisibility(visibility);
    }

    @Override
    public void realign(UnitChoicesMenuComponent unitChoicesMenuComponent, Point center, double lineDistance) {
        int pixelWidth = (int)unitChoicesMenuComponent.getActivity().getResources().getDimension(R.dimen.unit_choice_scroll_width);
        int x = center.x - pixelWidth / 2;
        int y = (int) (center.y - lineDistance) - unitChoicesMenuComponent.getUnitChoicesMenu().getMeasuredHeight() - 5;
        unitChoicesMenuComponent.getUnitChoicesMenu().setX(x);
        unitChoicesMenuComponent.getUnitChoicesMenu().setY(y);

        if (playerService.get().checkFunds(Units.MEDIC.getCost())) {
            setVisibility(unitChoicesMenuComponent, Units.MEDIC, View.VISIBLE);
        } else {
            setVisibility(unitChoicesMenuComponent, Units.MEDIC, View.GONE);
        }
        if (playerService.get().checkFunds(Units.FOOT_SOLDIER.getCost())) {
            setVisibility(unitChoicesMenuComponent, Units.FOOT_SOLDIER, View.VISIBLE);
        } else {
            setVisibility(unitChoicesMenuComponent, Units.FOOT_SOLDIER, View.GONE);
        }
        if (playerService.get().checkFunds(Units.MARAUDER.getCost())) {
            setVisibility(unitChoicesMenuComponent, Units.MARAUDER, View.VISIBLE);
        } else {
            setVisibility(unitChoicesMenuComponent, Units.MARAUDER, View.GONE);
        }

        unitChoicesMenuComponent.getUnitChoicesMenu().setVisibility(View.VISIBLE);
        unitChoicesMenuComponent.getUnitChoices().invalidate();
    }

    @Override
    public void clear(UnitChoicesMenuComponent unitChoicesMenuComponent) {
        unitChoicesMenuComponent.getUnitChoicesMenu().setVisibility(View.GONE);
    }
}
