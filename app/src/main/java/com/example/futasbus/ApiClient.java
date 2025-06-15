package com.example.futasbus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
//    private static final String BASE_URL = "http://192.168.1.11:8085/FutaBus_Backend/";
//    "Máy Thông"
    private static final String BASE_URL = "http://192.168.1.18:8085/FutaBus_Backend/";
//    "Máy Dũng"

//    private static final String BASE_URL = "http://192.168.43.184:8085/FutaBus_Backend/";
//    private static final String BASE_URL = "http://192.168.79.1:8085/"; //Máy Thông
//    private static final String BASE_URL = "http://192.168.79.1:8085/FutaBus_Backend/"; //Máy Thông
    private static Retrofit retrofit = null;

    static Gson gson = new GsonBuilder()
            .setDateFormat("dd/MM/yyyy HH:mm:ss")
            .create();
    private static final SessionCookieJar cookieJar = new SessionCookieJar();
    public static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .cookieJar(cookieJar)
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }
    public static ApiService getApiService() {
        Gson gson = new GsonBuilder()
                .setDateFormat("dd/MM/yyyy HH:mm:ss")
                .create();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

        }
        return retrofit.create(ApiService.class);
    }
}

