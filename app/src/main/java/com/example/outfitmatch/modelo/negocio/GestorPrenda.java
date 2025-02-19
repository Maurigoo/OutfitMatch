package com.example.outfitmatch.modelo.negocio;

import com.example.outfitmatch.R;
import com.example.outfitmatch.modelo.entidad.Prenda;
import com.example.outfitmatch.modelo.persistencia.DaoPrenda;

import java.util.ArrayList;
import java.util.List;

public class GestorPrenda {

    private static GestorPrenda instance;

    private GestorPrenda(){
        super();
    }

    public static GestorPrenda getInstance() {
        return instance==null? instance= new GestorPrenda() : instance;
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
     * @param userId ID del usuario.
     * @param listener Callback que retorna el total y la lista de prendas.
     */
    public void obtenerTotalPrendas(String userId, OnTotalPrendasListener listener) {
        DaoPrenda.getInstance().ObtenerPrendaFirebase(userId, prendasFirebase -> {
            List<Prenda> prendasPredefinidas = obtenerPrendasPredefinidas();

            List<Prenda> todasLasPrendas = new ArrayList<>();
            todasLasPrendas.addAll(prendasFirebase);
            todasLasPrendas.addAll(prendasPredefinidas);

            listener.onTotalPrendas(todasLasPrendas.size(), todasLasPrendas);
        });
    }

    /**
     * Obtiene una lista de prendas predefinidas.
     *
     * @return Lista de prendas.
     */
    private List<Prenda> obtenerPrendasPredefinidas() {
        List<Prenda> prendas = new ArrayList<>();
        prendas.add(new Prenda(R.drawable.basic_shirt, "M", "Algodón", "Blanco"));
        prendas.add(new Prenda(R.drawable.pants1, "32", "Denim", "Azul"));
        prendas.add(new Prenda(R.drawable.shoes1, "42", "Cuero", "Marrón"));
        return prendas;
    }
}

