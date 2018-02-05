package com.example.mohamed.nabdpharmacy;


import android.content.Context;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mohamed on 01/10/17.
 */


class ApiClient {

    static Retrofit retrofit;
//    public static final String BASE_URL ="http://10.0.2.2/PharmacyApp/";
private static final String BASE_URL ="https://nabdpharmacy.000webhostapp.com/";

    static Retrofit getApiClient(Context context){
        if(retrofit ==null){
            int cacheSize = 10 * 1024 * 1024; // 10 MB
            Cache cache = new Cache(context.getCacheDir(), cacheSize);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .cache(cache)
                    .build();

            retrofit =new Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
