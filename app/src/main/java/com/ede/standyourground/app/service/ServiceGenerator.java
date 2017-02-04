package com.ede.standyourground.app.service;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Eddie on 2/1/2017.
 */

public class ServiceGenerator {

    private static final String BASE_URL = "http://10.0.2.2:8000/";

    private static Retrofit retrofit;

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder();

    public static <S> S createService(Class<S> serviceClass) {
        if (retrofit == null) {
            retrofit = builder.client(httpClient.build()).build();
        }

        return retrofit.create(serviceClass);
    }
}
