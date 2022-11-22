package com.example.m_hikeapp.httpnetwork;

import com.example.m_hikeapp.models.Hike;
import com.example.m_hikeapp.models.HikePayload;
import com.example.m_hikeapp.models.HikeResponsePayload;
import com.example.m_hikeapp.utils.Constants;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface MHikeRestAPI {
//    @Headers({"Accept: application/json", "Content-Type: application/json"})
//    @FormUrlEncoded
//    @POST("")
//    Call<String> uploadToCloud (@Field(Constants.USER_ID) String name,
//                                             @Field(Constants.HIKE_DETAILS_LIST) HikePayload hikes);

//    @Headers({"Accept: application/json", "Content-Type: application/json"})
//    @FormUrlEncoded
//    @POST()
//    Call<String> uploadToCloud (@Body HikePayload hikes);


    @Headers({"Accept: application/json", "Content-Type: application/json"})
//    @FormUrlEncoded
    @POST("COMP1424CoreWS/comp1424cw")
    Call<ResponseBody> uploadToCloud (@Body HikePayload hikes);
//
//    @FormUrlEncoded
//    @POST("COMP1424CoreWS/comp1424cw")
//    Call<ResponseBody> uploadToCloud(@Body Map<String, String> hike);
}
