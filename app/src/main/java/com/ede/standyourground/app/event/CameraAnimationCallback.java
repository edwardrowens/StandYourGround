package com.ede.standyourground.app.event;

import com.google.android.gms.maps.GoogleMap;

/**
 *
 */

public abstract class CameraAnimationCallback implements GoogleMap.CancelableCallback {

    public abstract void onAnimated();

    @Override
    public void onFinish() {
        onAnimated();
    }

    @Override
    public void onCancel() {

    }
}
