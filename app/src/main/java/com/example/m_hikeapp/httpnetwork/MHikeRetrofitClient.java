package com.example.m_hikeapp.httpnetwork;

import android.content.Context;

import com.example.m_hikeapp.utils.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MHikeRetrofitClient {

    public Context context;
    private static MHikeRetrofitClient mInstance;
    private Retrofit retrofit;

    public MHikeRetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized MHikeRetrofitClient getInstance(){
        if(mInstance == null){
            mInstance = new MHikeRetrofitClient();
        }
        return mInstance;
    }

    public MHikeRestAPI getMHikeRestApi(){
        return retrofit.create(MHikeRestAPI.class);
    }
}
