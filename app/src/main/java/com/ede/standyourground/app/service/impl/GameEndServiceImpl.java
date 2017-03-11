package com.ede.standyourground.app.service.impl;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.ede.standyourground.app.activity.MainActivity;
import com.ede.standyourground.app.activity.MapsActivity;
import com.ede.standyourground.app.service.api.GameEndService;
import com.ede.standyourground.app.ui.HealthBarComponent;
import com.ede.standyourground.game.framework.management.impl.WorldManager;
import com.ede.standyourground.game.model.Base;
import com.ede.standyourground.game.model.Unit;
import com.ede.standyourground.game.model.api.DeathListener;

import javax.inject.Inject;

import dagger.Lazy;

public class GameEndServiceImpl implements GameEndService {

    private final Lazy<WorldManager> worldManager;

    @Inject
    GameEndServiceImpl(Lazy<WorldManager> worldManager) {
        this.worldManager = worldManager;
    }

    public void registerEndGame(final Activity activity) {
        worldManager.get().registerDeathListener(new DeathListener() {
            @Override
            public void onDeath(Unit mortal) {
                ((HealthBarComponent) MapsActivity.getComponent(HealthBarComponent.class)).removeComponentElement(mortal.getId());
                if (mortal instanceof Base) {
                    Base base = (Base) mortal;
                    if (!base.isEnemy()) {
                        Toast.makeText(activity, "You Lose", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, "You Win", Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(activity, MainActivity.class);
                    activity.startActivity(intent);
                }
            }
        });
    }
}
