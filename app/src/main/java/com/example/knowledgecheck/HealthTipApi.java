package com.example.knowledgecheck;


import retrofit2.Call;
import retrofit2.http.GET;

public interface HealthTipApi {
    @GET("advice")
    Call<HealthTipResponse> getRandomHealthTip();
}