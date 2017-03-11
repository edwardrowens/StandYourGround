package com.ede.standyourground.app.ui;

import android.app.Activity;
import android.widget.RelativeLayout;

import com.ede.standyourground.R;

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

    @Override
    public Activity getActivity() {
        return activity;
    }

    @Override
    public void drawComponentElements() {
        for (ComponentElement healthBar : healthBars.values()) {
            healthBar.invalidate();
        }
    }
}
