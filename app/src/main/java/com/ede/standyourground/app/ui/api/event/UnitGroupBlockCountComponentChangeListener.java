package com.ede.standyourground.app.ui.api.event;

import android.view.ViewGroup;

import java.util.Set;
import java.util.UUID;

/**
 *
 */

public interface UnitGroupBlockCountComponentChangeListener {
    void onCountChange(ViewGroup container, Set<UUID> unitIds);
}
