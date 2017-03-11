package com.ede.standyourground.app.ui;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.ede.standyourground.R;
import com.ede.standyourground.game.model.Units;

import java.util.UUID;

public class Icon {

    private final UUID componentElementId;
    private final Drawable icon;
    private final ImageView container;

    public Icon(UUID componentElementId, Activity activity, Units units) {
        this.componentElementId = componentElementId;
        container = (ImageView) LayoutInflater.from(activity).inflate(R.layout.icon_image, null);

        Drawable drawableIcon = null;
        try {
            drawableIcon = Drawable.createFromXml(activity.getResources(), activity.getResources().getXml(units.getDrawableId()));
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
