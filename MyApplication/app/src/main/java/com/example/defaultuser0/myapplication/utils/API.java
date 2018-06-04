package com.example.defaultuser0.myapplication.utils;

import com.example.defaultuser0.myapplication.constants.Constants;
import com.example.defaultuser0.myapplication.webservice.Services;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class API {

    static API instance;
    private Services mServices;

    private API() {
        setRestAdapter();
    }

    public static API getInstance() {
        if (instance == null) {
            return new API();
        } else return instance;
    }

    public Services getServices() {
        return mServices;
    }

    private void setRestAdapter() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.ENDPOINT)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        setServices(retrofit.create(Services.class));
    }

    private void setServices(Services mServices) {
        this.mServices = mServices;
    }
}