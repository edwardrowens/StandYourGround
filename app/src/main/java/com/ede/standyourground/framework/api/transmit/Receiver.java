package com.ede.standyourground.framework.api.transmit;

import android.os.Bundle;

/**
 * Created by Eddie on 2/11/2017.
 */

public interface Receiver {
    void onReceiveResult(int resultCode, Bundle resultData);
}
