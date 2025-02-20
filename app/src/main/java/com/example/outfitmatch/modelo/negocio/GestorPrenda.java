package com.example.outfitmatch.modelo.negocio;

import android.util.Log;

import com.example.outfitmatch.R;
import com.example.outfitmatch.modelo.entidad.Prenda;
import com.example.outfitmatch.modelo.persistencia.DaoPrenda;

import java.util.ArrayList;
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
     * Obtiene el total de prendas combinando las predefinidas y las de Firebase.
     *
     * @param userId   ID del usuario.
     * @param listener Callback que retorna el total y la lista de prendas.
     */
    public void obtenerTotalPrendas(String userId, OnTotalPrendasListener listener) {
        DaoPrenda.getInstance().ObtenerPrendaFirebase(userId, prendasFirebase -> {
            Log.d("GestorPrenda", "Prendas desde Firebase: " + prendasFirebase.size());

            List<Prenda> prendasPredefinidas = obtenerPrendasPredefinidas("All");
            Log.d("GestorPrenda", "Prendas predefinidas: " + prendasPredefinidas.size());

            List<Prenda> todasLasPrendas = new ArrayList<>();
            todasLasPrendas.addAll(prendasFirebase);
            todasLasPrendas.addAll(prendasPredefinidas);

            Log.d("GestorPrenda", "Total de prendas: " + todasLasPrendas.size());

            listener.onTotalPrendas(todasLasPrendas.size(), todasLasPrendas);
        });
    }

    /**
     * Obtiene una lista de prendas predefinidas filtradas por categoría.
     *
     * @param category La categoría seleccionada (Shirts, Pants, Shoes, etc.).
     * @return Lista de prendas filtradas por categoría.
     */
    private List<Prenda> obtenerPrendasPredefinidas(String category) {
        List<Prenda> prendas = new ArrayList<>();

        // Asegúrate de que todas las categorías se estén agregando
        switch (category) {
            case "Shirts":
                prendas.add(new Prenda(R.drawable.basic_shirt, "M", "Algodón", "Blanco", "Shirts"));
                prendas.add(new Prenda(R.drawable.basic_shirt, "L", "Poliéster", "Negro", "Shirts"));
                break;
            case "Pants":
                prendas.add(new Prenda(R.drawable.pants1, "32", "Denim", "Azul", "Pants"));
                prendas.add(new Prenda(R.drawable.pants2, "34", "Lana", "Negro", "Pants"));
                break;
            case "Shoes":
                prendas.add(new Prenda(R.drawable.shoes1, "42", "Cuero", "Marrón", "Shoes"));
                prendas.add(new Prenda(R.drawable.shoes2, "43", "Suela", "Negro", "Shoes"));
                break;
            case "Dresses":
                prendas.add(new Prenda(R.drawable.dress1, "S", "Seda", "Rojo", "Dresses"));
                prendas.add(new Prenda(R.drawable.dress2, "M", "Algodón", "Verde", "Dresses"));
                break;
            case "Accessories":
                prendas.add(new Prenda(R.drawable.accessory1, "Tamaño Único", "Plástico", "Dorado", "Accessories"));
                break;
            case "All":
                // Asegúrate de que se agreguen todas las categorías aquí
                prendas.add(new Prenda(R.drawable.basic_shirt, "M", "Algodón", "Blanco", "Shirts"));
                prendas.add(new Prenda(R.drawable.pants1, "32", "Denim", "Azul", "Pants"));
                prendas.add(new Prenda(R.drawable.shoes1, "42", "Cuero", "Marrón", "Shoes"));
                prendas.add(new Prenda(R.drawable.dress1, "S", "Seda", "Rojo", "Dresses"));
                prendas.add(new Prenda(R.drawable.accessory1, "Tamaño Único", "Plástico", "Dorado", "Accessories"));
                break;
            default:
                break;
        }

        return prendas;
    }
}