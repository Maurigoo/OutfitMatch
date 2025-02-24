package com.example.outfitmatch;

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
