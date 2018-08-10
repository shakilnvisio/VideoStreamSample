package com.nvisio.video.videostreamsample.network;

import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static String BASE_URL = "https://www.googleapis.com/youtube/v3/";
    private static OkHttpClient httpClient=new OkHttpClient.Builder().build();
    private static Retrofit.Builder builder=new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    private static Retrofit retrofit=builder.build();

    public RetrofitInstance() {
    }

    public static void changeApiBaseUrl(String newApiBaseUrl){
        BASE_URL = newApiBaseUrl;
        builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        retrofit = builder.build();
    }


    public static String getBaseUrl(){
        return BASE_URL;
    }
    public static void changeBaseUrlToDefualt(){
        BASE_URL="https://www.googleapis.com/youtube/v3/";
    }

    public static <S> S createService(Class<S> serviceClass){
        return retrofit.create(serviceClass);
    }
}
