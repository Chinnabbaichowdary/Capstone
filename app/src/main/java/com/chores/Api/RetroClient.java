package com.chores.Api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroClient {

    private static final String ROOT_URL = "http://myprojectsworks.com/";
    public static Retrofit getRetrofitInstance() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        return new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public static com.chores.Api.ApiService getApiService() {
        return getRetrofitInstance().create(com.chores.Api.ApiService.class);

    }
}
