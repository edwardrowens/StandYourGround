package com.ede.standyourground.app.ui;

import android.app.Activity;

import java.util.UUID;


public interface Component {
    void addComponentElement(ComponentElement componentElement);
    void removeComponentElement(UUID componentElementId);
    ComponentElement getElement(UUID componentElementId);
    Activity getActivity();
    void drawComponentElements();
}
