package com.example.microtemp.microblog.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL =  "http://212.191.92.88:51020/";
    private static RetrofitClient mInstance;
    private Retrofit retrofit;


    public RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClient getmInstance() {

        if(mInstance == null){
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public API getAPI(){
        return retrofit.create(API.class);
    }


}
