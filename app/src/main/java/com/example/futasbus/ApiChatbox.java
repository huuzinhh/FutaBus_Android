package com.example.futasbus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiChatbox {
    private static final String BASE_URL_2 = "http://192.168.17.1:5000/";

    private static Retrofit retrofit2 = null;

    static Gson gson = new GsonBuilder().create();

    public static Retrofit getClient2() {
        if (retrofit2 == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build();

            retrofit2 = new Retrofit.Builder()
                    .baseUrl(BASE_URL_2)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit2;
    }

    public static ApiServiceChatbox getApiServiceChatbox() {
        return getClient2().create(ApiServiceChatbox.class);
    }
}
