package com.ede.standyourground.app.ui.impl.component;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ede.standyourground.R;
import com.ede.standyourground.app.ui.api.component.ComponentElement;
import com.ede.standyourground.game.api.model.UnitType;

import java.util.UUID;

public class Icon extends ComponentElement {

    private final UUID componentElementId;
    private final Drawable icon;
    private final ImageView container;

    public Icon(UUID componentElementId, Activity activity, UnitType unitType, ViewGroup parent) {
        super(activity.getApplicationContext());
        this.componentElementId = componentElementId;
        container = (ImageView) LayoutInflater.from(activity).inflate(R.layout.icon_image, parent).findViewById(R.id.iconImage);

        Drawable drawableIcon = null;
        try {
            drawableIcon = Drawable.createFromXml(activity.getResources(), activity.getResources().getXml(unitType.getFriendlyDrawableId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        icon = drawableIcon;
        container.setBackground(icon);
    }

    public Drawable getIcon() {
        return icon;
    }

    public ImageView getContainer() {
        return container;
    }

    public UUID getComponentElementId() {
        return componentElementId;
    }
}
