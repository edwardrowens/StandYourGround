package com.ede.standyourground.networking.api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    public static final String BASE_URL = "https://standyourground.herokuapp.com/";
//    public static final String BASE_URL = "http://192.168.0.102:8000/";

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
