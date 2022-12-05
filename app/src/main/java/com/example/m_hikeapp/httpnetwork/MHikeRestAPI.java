package com.example.m_hikeapp.httpnetwork;
import com.example.m_hikeapp.models.HikePayload;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface MHikeRestAPI {
    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @POST("COMP1424CoreWS/comp1424cw")
    Call<ResponseBody> uploadToCloud (@Body HikePayload hikes);
}
