package com.ede.standyourground.framework.impl.service;

import android.content.Context;
import android.util.TypedValue;

import com.ede.standyourground.framework.api.service.GraphicService;

import javax.inject.Inject;

/**
 *
 */

public class GraphicServiceImpl implements GraphicService {

    @Inject
    GraphicServiceImpl() {

    }

    @Override
    public int dpToPixel(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    @Override
    public int spToPixel(float sp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    @Override
    public int dpToSp(float dp, Context context) {
        return (int) (dpToPixel(dp, context) / (float) spToPixel(dp, context));
    }
}
