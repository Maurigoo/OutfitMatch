package com.example.outfitmatch.modelo.persistencia;

import android.util.Log;

import com.example.outfitmatch.modelo.entidad.Prenda;

import java.util.List;

public class DaoPrenda {

    private static DaoPrenda instance;

    private DaoPrenda() {
        // Constructor vacío
    }

    public static DaoPrenda getInstance() {
        return instance == null ? instance = new DaoPrenda() : instance;
    }

    /**
     * Método que simula la obtención de prendas desde Firebase.
     *
     * @param userId   El ID del usuario.
     * @param listener El listener para devolver las prendas obtenidas.
     */
    public void ObtenerPrendaFirebase(String userId, OnPrendasListener listener) {
        // Simulamos la obtención de prendas desde Firebase (o cualquier otra fuente de datos).
        List<Prenda> prendas = List.of(
                new Prenda(0, "M", "Algodón", "Blanco", "Shirts"),
                new Prenda(0, "32", "Denim", "Azul", "Pants"),
        new Prenda(0, "32", "Denim", "Azul", "Pants")

        );

        // Llamamos al listener con las prendas obtenidas (simulación).
        listener.onPrendasObtenidas(prendas);
    }

    public interface OnPrendasListener {
        void onPrendasObtenidas(List<Prenda> prendas);
    }
}