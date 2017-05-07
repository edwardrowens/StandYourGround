package com.ede.standyourground.framework.impl.service;

import android.content.Context;
import android.graphics.Color;
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
    public float hexToHue(String hex) {
        int r = 0, g = 0, b = 0;
        switch (hex.length()) {
            case 7:
                r = Integer.parseInt(hex.substring(1, 3), 16);
                g = Integer.parseInt(hex.substring(3, 5), 16);
                b = Integer.parseInt(hex.substring(5, 7), 16);
                break;
            case 9:
                r = Integer.parseInt(hex.substring(3, 5), 16);
                g = Integer.parseInt(hex.substring(5, 7), 16);
                b = Integer.parseInt(hex.substring(7, 9), 16);
                break;
        }
        float[] hsl = new float[3];
        Color.RGBToHSV(r, g, b, hsl);

        return hsl[0];
    }

    @Override
    public int dpToSp(float dp, Context context) {
        return (int) (dpToPixel(dp, context) / (float) spToPixel(dp, context));
    }
}
