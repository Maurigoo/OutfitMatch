package com.example.outfitmatch.modelo.negocio;

import android.util.Log;

import com.example.outfitmatch.modelo.entidad.Prenda;
import com.example.outfitmatch.modelo.persistencia.DaoPrenda;

import java.util.List;

public class GestorPrenda {

    private static GestorPrenda instance;

    private GestorPrenda() {
        super();
    }

    public static GestorPrenda getInstance() {
        return instance == null ? instance = new GestorPrenda() : instance;
    }

    /**
     * Interface para callback al obtener el total de prendas.
     */
    public interface OnTotalPrendasListener {
        void onTotalPrendas(int total, List<Prenda> prendas);
    }

    /**
     * Obtiene las prendas de Firebase en tiempo real y devuelve el total.
     *
     * @param userId   El UID del usuario.
     * @param listener El listener para devolver el total y la lista de prendas.
     */
    public void obtenerPrendasSoloFirebase(String userId, OnTotalPrendasListener listener) {
        DaoPrenda.getInstance().ObtenerPrendaFirebase(userId, new DaoPrenda.OnPrendasListener() {
            @Override
            public void onPrendasObtenidas(List<Prenda> prendas) {
                // Obtener el total de prendas
                int total = prendas.size();
                Log.d("GestorPrenda", "Total de prendas: " + total);

                // Llamar al listener con el total y la lista de prendas
                listener.onTotalPrendas(total, prendas);
            }
        });
    }
}