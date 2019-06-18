package com.vadimfedchuk1994gmail.weather.network;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vadimfedchuk1994gmail.weather.R;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit = null;
    private static OkHttpClient okHttpClient;

    public static Retrofit getClient(Context context) {

        if (okHttpClient == null) {
            okHttpClient = provideOkHttpClient();
        }

        if (retrofit == null) {
            return new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(provideGson()))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(context.getResources()
                            .getString(R.string.base_url_upload_data_weather))
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }

    public static OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    public static Gson provideGson() {
        return new GsonBuilder()
                .setLenient()
                .create();
    }
}
