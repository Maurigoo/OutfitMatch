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

    public void ObtenerPrendaFirebase(String userId, OnPrendasListener listener) {
        // Este es un ejemplo. Aquí deberías obtener los datos desde Firebase o alguna otra fuente.
        // Vamos a simular que obtenemos dos prendas de Firebase para este ejemplo.
        List<Prenda> prendas = List.of(
                new Prenda(0, "M", "Algodón", "Blanco", "Shirts"),
                new Prenda(0, "32", "Denim", "Azul", "Pants")
        );

        // Llamamos al listener con las prendas obtenidas
        listener.onPrendasObtenidas(prendas);
    }

    public interface OnPrendasListener {
        void onPrendasObtenidas(List<Prenda> prendas);
    }
}