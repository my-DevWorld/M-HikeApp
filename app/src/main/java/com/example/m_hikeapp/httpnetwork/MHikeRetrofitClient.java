package com.example.m_hikeapp.httpnetwork;

import android.content.Context;

import com.example.m_hikeapp.utils.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MHikeRetrofitClient {

    public Context context;
    private static MHikeRetrofitClient mInstance;
    private OkHttpClient okHttpClient;
    private Retrofit retrofit;

    public MHikeRetrofitClient() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient.addInterceptor(interceptor);

        okHttpClient = httpClient.build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(okHttpClient)
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
