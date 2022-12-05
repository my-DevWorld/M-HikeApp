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
//        Set network timeout
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(Constants.NETWORK_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constants.NETWORK_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Constants.NETWORK_TIMEOUT, TimeUnit.SECONDS);

//        intercept
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
//    Check for if there is an instance of this class
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
