package com.ede.standyourground.framework.impl.service;

import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.ede.standyourground.framework.api.service.ViewService;

import javax.inject.Inject;

/**
 *
 */

public class ViewServiceImpl implements ViewService {

    @Inject
    public ViewServiceImpl() {

    }

    @Override
    public ViewTreeObserver.OnGlobalLayoutListener centerViewGroup(final ViewGroup viewGroup, final Point center, final double pixelRadius, final int width) {
        final Point point = new Point();
        point.x = center.x - (int) Math.round(pixelRadius) - 5 - (width / 2) + (int) Math.round(pixelRadius);

        ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                point.y = center.y - (int) Math.round(pixelRadius) - 5 - viewGroup.getMeasuredHeight();
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.leftMargin = point.x;
                layoutParams.topMargin = point.y;
                viewGroup.setLayoutParams(layoutParams);
                viewGroup.setVisibility(View.VISIBLE);
            }
        };

        viewGroup.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
        viewGroup.bringToFront();
        return onGlobalLayoutListener;
    }

    @Override
    public ViewTreeObserver.OnGlobalLayoutListener centerViewGroup(final ViewGroup viewGroup, final Point center, final double pixelRadius) {
        ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final Point point = new Point();
                point.y = center.y - (int) Math.round(pixelRadius) - 5 - viewGroup.getMeasuredHeight();
                point.x = center.x - (int) Math.round(pixelRadius) - 5 - (viewGroup.getMeasuredWidth() / 2) + (int) Math.round(pixelRadius);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(viewGroup.getMeasuredWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.leftMargin = point.x;
                layoutParams.topMargin = point.y;
                viewGroup.setLayoutParams(layoutParams);
                viewGroup.setVisibility(View.VISIBLE);
            }
        };

        viewGroup.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
        viewGroup.bringToFront();
        return onGlobalLayoutListener;
    }
}
