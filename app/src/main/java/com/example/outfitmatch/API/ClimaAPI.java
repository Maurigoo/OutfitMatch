package com.example.outfitmatch.API;

import com.example.outfitmatch.Clima;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interfaz para definir los métodos de la API de clima utilizando Retrofit.
 * Esta interfaz especifica cómo se interactúa con los servicios web de la API para obtener datos del clima.
 */
public interface ClimaAPI {

    /**
     * Método para obtener la información actual del clima de una ubicación específica.
     *
     * @param apiKey La clave de la API para autenticar la solicitud.
     * @param location La ubicación (ciudad, coordenadas, etc.) para la cual se desea obtener el clima.
     * @return Un objeto Call de Retrofit que, cuando se ejecute, devolverá la información del clima.
     */
    @GET("current.json")
    Call<Clima> getClima(
            @Query("key") String apiKey,  // Parámetro de consulta para la clave de la API
            @Query("q") String location   // Parámetro de consulta para la ubicación (por ejemplo, ciudad)
    );
}
