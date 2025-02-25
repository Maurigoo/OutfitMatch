package com.example.outfitmatch.modelo.negocio;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.outfitmatch.modelo.entidad.Prenda;
import com.example.outfitmatch.modelo.persistencia.DaoPrenda;

import java.util.List;

/**
 * GestorPrenda gestiona la lógica de negocio relacionada con las prendas.
 */
public class GestorPrenda {

    private static GestorPrenda instance;

    private GestorPrenda() {
        super();
    }

    /**
     * Singleton para obtener la instancia única de GestorPrenda.
     *
     * @return Instancia de GestorPrenda.
     */
    public static GestorPrenda getInstance() {
        return instance == null ? instance = new GestorPrenda() : instance;
    }

    /**
     * Interface para manejar el resultado de la obtención de prendas.
     */
    public interface OnTotalPrendasListener {
        /**
         * Llamado cuando se obtienen las prendas exitosamente.
         *
         * @param total   Número total de prendas.
         * @param prendas Lista de prendas obtenidas.
         */
        void onTotalPrendas(int total, List<Prenda> prendas);

        /**
         * Llamado cuando ocurre un error al obtener las prendas.
         *
         * @param e Excepción que describe el error.
         */
        void onError(@NonNull Exception e);
    }

    /**
     * Obtiene las prendas del usuario desde Firebase Firestore.
     *
     * @param userId   El UID del usuario autenticado.
     * @param listener Listener para devolver el total de prendas o manejar errores.
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

            @Override
            public void onError(@NonNull Exception e) {
                // Manejar error y pasarlo al listener
                Log.e("GestorPrenda", "Error al obtener prendas", e);
                listener.onError(e);
            }
        });
    }
}
