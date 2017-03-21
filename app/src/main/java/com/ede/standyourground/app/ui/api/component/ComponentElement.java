package com.ede.standyourground.app.ui.api.component;


import android.content.Context;
import android.view.View;

import java.util.UUID;

public abstract class ComponentElement extends View {
    public ComponentElement(Context context) {
        super(context);
    }

    public abstract UUID getComponentElementId();
}
