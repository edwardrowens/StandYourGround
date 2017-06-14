package com.ede.standyourground.framework.api.service;

import com.ede.standyourground.app.event.CameraAnimationCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

/**
 *
 */

public interface CameraService {
    CameraPosition focusCamera(LatLng p1, LatLng p2, CameraAnimationCallback cameraAnimationCallback);
}
