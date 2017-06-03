package com.example.android.foodfinder.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ak00481127 on 5/10/2017.
 */

public class ApiClient {

    private static Retrofit retrofit = null;

    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/";

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()).build();

        }
        return retrofit;
    }
}
