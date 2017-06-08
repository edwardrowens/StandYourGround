package com.ede.standyourground.app.ui.api.component;

import android.app.Activity;
import android.graphics.PointF;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ede.standyourground.R;
import com.ede.standyourground.app.ui.api.service.UnitGroupBlockHealthBarComponentService;
import com.ede.standyourground.app.ui.impl.component.HealthBar;
import com.ede.standyourground.app.ui.impl.component.Icon;
import com.ede.standyourground.app.ui.impl.component.UnitGroupBlockHealthBarComponent;
import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.game.api.event.listener.OnDeathListener;
import com.ede.standyourground.game.api.model.Unit;
import com.ede.standyourground.game.api.model.UnitType;
import com.ede.standyourground.game.api.service.UnitService;

import java.util.UUID;

import javax.inject.Inject;

import dagger.Lazy;

/**
 *
 */

public class UnitGroupBlockHealthBarComponentFactory {

    private static final Logger logger = new Logger(UnitGroupBlockHealthBarComponentFactory.class);

    private final Lazy<UnitService> unitService;
    private final Lazy<UnitGroupBlockHealthBarComponentService> unitGroupBlockHealthBarComponentService;

    @Inject
    public UnitGroupBlockHealthBarComponentFactory(Lazy<UnitService> unitService,
                                                   Lazy<UnitGroupBlockHealthBarComponentService> unitGroupBlockHealthBarComponentService) {
        this.unitService = unitService;
        this.unitGroupBlockHealthBarComponentService = unitGroupBlockHealthBarComponentService;
    }

    public UnitGroupBlockHealthBarComponent createUnitGroupBlockHealthBar(final Activity activity, ViewGroup parent, final UUID unitId, float healthPercentage, UnitType unitType) {
        logger.i("Creating unit group block health bar with ID <{%s}>, health <{%f}>, and unit type <{%s}>", unitId.toString(), healthPercentage, unitType.toString());
        HealthBar healthBar = new HealthBar(unitId, activity);
        healthBar.setHealthPercentage(healthPercentage);
        healthBar.setWidth(100);
        healthBar.setHeight(50);
        healthBar.setPoint(new PointF(0f, 0f));

        final ViewGroup container = (ViewGroup) LayoutInflater.from(activity).inflate(R.layout.unit_group_block, parent).findViewById(R.id.unitGroupBlock);

        // Create the icon
        ViewGroup iconContainer = (ViewGroup) LayoutInflater.from(activity).inflate(R.layout.unit_group_block_icon, container).findViewById(R.id.unitGroupBlockIcon);
        Icon icon = new Icon(UUID.randomUUID(), activity, unitType);
        iconContainer.addView(icon.getContainer());

        // Create the health bar
        ViewGroup healthBarContainer = (ViewGroup) LayoutInflater.from(activity).inflate(R.layout.unit_group_block_health_bar, null);
        healthBarContainer.setLayoutParams(new ViewGroup.LayoutParams((int) healthBar.getHealthBarBorder().width(), (int) healthBar.getHealthBarBorder().height()));
        healthBarContainer.addView(healthBar);
        container.addView(healthBarContainer);

        final UnitGroupBlockHealthBarComponent unitGroupBlockHealthBarComponent = new UnitGroupBlockHealthBarComponent(activity, UUID.randomUUID(), unitId, container);

        final OnDeathListener onDeathListenerHook = new OnDeathListener() {
            @Override
            public void onDeath(final Unit mortal, Unit killer) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (unitId.equals(mortal.getId())) {
                            unitGroupBlockHealthBarComponentService.get().clear(unitGroupBlockHealthBarComponent);
                        }
                    }
                });
            }
        };

        unitGroupBlockHealthBarComponent.setOnDeathListenerHook(onDeathListenerHook);
        unitService.get().registerOnDeathListener(onDeathListenerHook);

        return unitGroupBlockHealthBarComponent;
    }
}
