package com.ede.standyourground.framework.api.transmit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Eddie on 2/12/2017.
 */

public class EmptyCallback<T> implements Callback<T> {
    @Override
    public void onResponse(Call<T> call, Response<T> response) {}

    @Override
    public void onFailure(Call<T> call, Throwable t) {}
}
