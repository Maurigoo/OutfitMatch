package com.example.outfitmatch.API;

import com.example.outfitmatch.Clima;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ClimaAPI {
    @GET("current.json")
    Call<Clima> getClima(
            @Query("key") String apiKey,
            @Query("q") String location
    );
}
