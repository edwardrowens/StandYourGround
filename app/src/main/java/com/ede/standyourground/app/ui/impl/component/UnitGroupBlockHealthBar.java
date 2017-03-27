package com.ede.standyourground.app.ui.impl.component;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ede.standyourground.R;
import com.ede.standyourground.app.ui.api.component.UnitGroupBlock;
import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.framework.api.dagger.application.MyApp;
import com.ede.standyourground.game.api.event.listener.OnDeathListener;
import com.ede.standyourground.game.api.model.Unit;
import com.ede.standyourground.game.api.model.Units;

import java.util.List;
import java.util.UUID;

/**
 *
 */
public class UnitGroupBlockHealthBar extends UnitGroupBlock {

    private static final Logger logger = new Logger(UnitGroupBlockHealthBar.class);

    private final RelativeLayout healthBarContainer;

    public UnitGroupBlockHealthBar(UUID componentElementId, final List<UUID> unitIds, Activity activity, Units units, final HealthBar healthBar) {
        super(componentElementId, unitIds, activity, units);
        this.healthBarContainer = (RelativeLayout) LayoutInflater.from(activity).inflate(R.layout.unit_group_block_health_bar, null);

        healthBarContainer.setLayoutParams(new ViewGroup.LayoutParams((int) healthBar.getHealthBarBorder().width(), (int) healthBar.getHealthBarBorder().height()));
        this.healthBarContainer.addView(healthBar);
        this.container.addView(healthBarContainer);

        MyApp.getAppComponent().getUnitService().get().registerOnDeathListener(new OnDeathListener() {
            @Override
            public void onDeath(final Unit mortal, final Unit killer) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (unitIds.contains(mortal.getId())) {
                            clear();
                        }
                    }
                });
            }
        });
    }

    @Override
    public Activity getActivity() {
        return activity;
    }

    @Override
    public void drawComponentElements() {
        this.healthBarContainer.postInvalidate();
        this.container.postInvalidate();
        this.iconContainer.postInvalidate();
    }

    @Override
    protected void setVisible(int visibility) {
        healthBarContainer.setVisibility(visibility);
    }

    @Override
    protected void clearViews() {
        healthBarContainer.removeAllViews();
        healthBarContainer.setVisibility(View.GONE);
    }

    public LinearLayout getContainer() {
        return container;
    }
}
