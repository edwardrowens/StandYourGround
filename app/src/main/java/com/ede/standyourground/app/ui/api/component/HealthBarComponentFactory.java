package com.ede.standyourground.app.ui.api.component;

import android.app.Activity;

import com.ede.standyourground.app.ui.api.service.HealthBarService;
import com.ede.standyourground.app.ui.impl.component.HealthBar;
import com.ede.standyourground.app.ui.impl.component.HealthBarComponent;
import com.ede.standyourground.game.api.event.listener.OnDeathListener;
import com.ede.standyourground.game.api.event.listener.PositionChangeListener;
import com.ede.standyourground.game.api.event.listener.UnitCreatedListener;
import com.ede.standyourground.game.api.model.MovableUnit;
import com.ede.standyourground.game.api.model.Unit;
import com.ede.standyourground.game.api.service.UnitService;

import javax.inject.Inject;

import dagger.Lazy;

public class HealthBarComponentFactory {

    private final Lazy<UnitService> unitService;
    private final Lazy<HealthBarService> healthBarService;

    @Inject
    HealthBarComponentFactory(Lazy<UnitService> unitService,
                              Lazy<HealthBarService> healthBarService) {
        this.unitService = unitService;
        this.healthBarService = healthBarService;
    }

    public HealthBarComponent createHealthBarComponent(final Activity activity) {
        final HealthBarComponent healthBarComponent = new HealthBarComponent(activity);
        unitService.get().registerUnitCreatedListener(new UnitCreatedListener() {
            @Override
            public void onUnitCreated(final Unit unit) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (unit.isVisible()) {
                            HealthBar healthBar = new HealthBar(unit.getId(), activity.getApplicationContext());
                            healthBarService.get().setHealthBarPosition(unit, healthBar);
                            healthBarComponent.addComponentElement(healthBar);
                        }
                    }
                });
            }
        });

        unitService.get().registerPositionChangeListener(new PositionChangeListener() {
            @Override
            public void onPositionChange(final MovableUnit movableUnit) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HealthBar healthBar = healthBarComponent.getElement(movableUnit.getId());
                        if (healthBar == null) {
                            if (movableUnit.isVisible()) {
                                healthBar = new HealthBar(movableUnit.getId(), activity);
                                healthBarService.get().setHealthBarPosition(movableUnit, healthBar);
                                healthBarComponent.addComponentElement(healthBar);
                            }
                        } else {
                            if (!movableUnit.isVisible()) {
                                healthBarComponent.removeComponentElement(movableUnit.getId());
                            } else {
                                healthBarService.get().setHealthBarPosition(movableUnit, healthBar);
                            }
                        }
                    }
                });
            }
        });

        unitService.get().registerOnDeathListener(new OnDeathListener() {
            @Override
            public void onDeath(final Unit mortal) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (healthBarComponent.containsHealthBar(mortal.getId())) {
                            healthBarComponent.removeComponentElement(mortal.getId());
                        }
                    }
                });
            }
        });
        return healthBarComponent;
    }
}
