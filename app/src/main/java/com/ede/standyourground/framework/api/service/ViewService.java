package com.ede.standyourground.framework.api.service;

import android.graphics.Point;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

/**
 *
 */

public interface ViewService {
    ViewTreeObserver.OnGlobalLayoutListener centerViewGroup(ViewGroup viewGroup, final Point center, final double pixelRadius, int width);
    ViewTreeObserver.OnGlobalLayoutListener centerViewGroup(ViewGroup viewGroup, final Point center, final double pixelRadius);
}
