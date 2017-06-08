package com.ede.standyourground.app.ui.impl.component;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ede.standyourground.R;
import com.ede.standyourground.app.ui.api.component.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class HealthBarComponent implements Component {

    private final Map<UUID, HealthBar> healthBars = new ConcurrentHashMap<>();
    private final Activity activity;

    public HealthBarComponent(Activity activity) {
        this.activity = activity;
    }

    public void addComponentElement(HealthBar healthBar) {
        healthBars.put(healthBar.getComponentElementId(), healthBar);
        RelativeLayout linearLayout = (RelativeLayout) activity.findViewById(R.id.mapContainer);
        linearLayout.addView(healthBar);
    }

    public void removeComponentElement(UUID componentElementId) {
        RelativeLayout linearLayout = (RelativeLayout) activity.findViewById(R.id.mapContainer);
        linearLayout.removeView(healthBars.get(componentElementId));
        healthBars.remove(componentElementId);
    }

    public HealthBar getElement(UUID componentElementId) {
        return healthBars.get(componentElementId);
    }

    public boolean containsHealthBar(UUID componentElementId) {
        return healthBars.containsKey(componentElementId);
    }

    @Override
    public Activity getActivity() {
        return activity;
    }

    @Override
    public ViewGroup getContainer() {
        return null;
    }
}
