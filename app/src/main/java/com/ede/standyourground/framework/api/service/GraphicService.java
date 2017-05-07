package com.ede.standyourground.framework.api.service;

import android.content.Context;

/**
 *
 */

public interface GraphicService {
    int dpToPixel(float dp, Context context);
    int spToPixel(float sp, Context context);
    float hexToHue(String hex);
    int dpToSp(float dp, Context context);
}
