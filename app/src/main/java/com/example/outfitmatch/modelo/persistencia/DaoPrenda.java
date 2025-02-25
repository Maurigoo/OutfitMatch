package com.example.outfitmatch.modelo.persistencia;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.outfitmatch.modelo.entidad.Prenda;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar las operaciones relacionadas con las prendas en Firestore.
 */
public class DaoPrenda {

    private static DaoPrenda instance;
    private final FirebaseFirestore db;

    private DaoPrenda() {
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Singleton para obtener la instancia única del DaoPrenda.
     *
     * @return Instancia de DaoPrenda.
     */
    public static DaoPrenda getInstance() {
        return instance == null ? instance = new DaoPrenda() : instance;
    }

    /**
     * Obtiene las prendas del usuario desde Firebase Firestore.
     *
     * @param userId   El ID del usuario autenticado.
     * @param listener Listener para devolver las prendas obtenidas o manejar errores.
     */
    public void ObtenerPrendaFirebase(String userId, OnPrendasListener listener) {
        db.collection("prendas").document(userId).collection("user_prendas")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Prenda> prendas = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String imagenUrl = document.getString("imagenUrl");
                            String talla = document.getString("talla");
                            String material = document.getString("material");
                            String color = document.getString("color");
                            String tipo = document.getString("tipo");

                            Prenda prenda = new Prenda(0, talla, material, color, tipo);
                            prenda.setImagenUrl(imagenUrl); // Asegúrate de que la clase Prenda tiene este campo.

                            prendas.add(prenda);
                        }

                        // Llamar al listener con las prendas obtenidas
                        listener.onPrendasObtenidas(prendas);
                    } else {
                        // Manejar error en la obtención
                        Log.e("Firestore", "Error al obtener prendas", task.getException());
                        listener.onError(task.getException());
                    }
                });
    }

    /**
     * Interfaz para manejar los resultados de la consulta a Firestore.
     */
    public interface OnPrendasListener {
        /**
         * Método llamado cuando las prendas se obtienen exitosamente.
         *
         * @param prendas Lista de prendas obtenidas.
         */
        void onPrendasObtenidas(List<Prenda> prendas);

        /**
         * Método llamado cuando ocurre un error al obtener las prendas.
         *
         * @param e Excepción lanzada durante la obtención.
         */
        void onError(@NonNull Exception e);
    }
}
