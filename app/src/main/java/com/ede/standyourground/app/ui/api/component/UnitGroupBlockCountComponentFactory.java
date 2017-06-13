package com.ede.standyourground.app.ui.api.component;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ede.standyourground.R;
import com.ede.standyourground.app.ui.api.event.ComponentChangeListener;
import com.ede.standyourground.app.ui.api.service.UnitGroupBlockCountComponentService;
import com.ede.standyourground.app.ui.impl.component.Icon;
import com.ede.standyourground.app.ui.impl.component.UnitGroupBlockCount;
import com.ede.standyourground.framework.api.Logger;
import com.ede.standyourground.game.api.event.listener.OnDeathListener;
import com.ede.standyourground.game.api.model.Unit;
import com.ede.standyourground.game.api.model.UnitType;
import com.ede.standyourground.game.api.service.UnitService;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;

import javax.inject.Inject;

import dagger.Lazy;

/**
 *
 */

public class UnitGroupBlockCountComponentFactory {

    private static final Logger logger = new Logger(UnitGroupBlockCountComponentFactory.class);

    private final Lazy<UnitService> unitService;
    private final Lazy<UnitGroupBlockCountComponentService> unitGroupBlockCountComponentService;

    @Inject
    public UnitGroupBlockCountComponentFactory(Lazy<UnitService> unitService,
                                               Lazy<UnitGroupBlockCountComponentService> unitGroupBlockCountComponentService) {
        this.unitService = unitService;
        this.unitGroupBlockCountComponentService = unitGroupBlockCountComponentService;
    }

    public UnitGroupBlockCount createUnitGroupBlockCountComponent(final Activity activity, final Set<UUID> unitIds, UnitType unitType) {
        logger.i("Creating a unit group block count with <{%s}> IDs of type <{%s}>", unitIds.toString(), unitType.toString());
        final ViewGroup container = (ViewGroup) LayoutInflater.from(activity).inflate(R.layout.unit_group_block, null);

        // Create the icon
        ViewGroup iconContainer = (ViewGroup) LayoutInflater.from(activity).inflate(R.layout.unit_group_block_icon, container).findViewById(R.id.unitGroupBlockIcon);
        new Icon(UUID.randomUUID(), activity, unitType, iconContainer);

        // Create the count container
        final TextView countContainer = (TextView) LayoutInflater.from(activity).inflate(R.layout.text_view_component, container).findViewById(R.id.textViewComponent);
        countContainer.setText(activity.getResources().getString(R.string.unitGroupCountText, unitIds.size()));

        final UnitGroupBlockCount unitGroupBlockCountComponent = new UnitGroupBlockCount(activity, UUID.randomUUID(), new ConcurrentSkipListSet<>(unitIds), container, countContainer);

        OnDeathListener onDeathListener = new OnDeathListener() {
            @Override
            public void onDeath(final Unit mortal, final Unit killer) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        logger.i("Removing <{%s}> from the unit group block count", mortal.getId());
                        if (unitGroupBlockCountComponent.getUnitIds().remove(mortal.getId())) {
                            final int count = unitGroupBlockCountComponent.getUnitIds().size();
                            logger.i("Removed <{%s}> from the unit group block count. Count is now <{%d}>", mortal.getId(), count);
                            unitGroupBlockCountComponentService.get().setCount(unitGroupBlockCountComponent, count);
                            for (ComponentChangeListener unitGroupBlockCountComponentChangeListener : unitGroupBlockCountComponent.getComponentChangeListeners()) {
                                unitGroupBlockCountComponentChangeListener.onComponentChange(unitGroupBlockCountComponent);
                            }
                        } else {
                            logger.i("Could not remove <{%s}> from the unit group block count. Count is <{%d}>", mortal.getId(), unitGroupBlockCountComponent.getUnitIds().size());
                        }
                    }
                });
            }
        };

        unitService.get().registerOnDeathListener(onDeathListener);

        unitGroupBlockCountComponent.setOnDeathListenerHook(onDeathListener);
        return unitGroupBlockCountComponent;
    }

}
