package com.example.outfitmatch.adaptador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.outfitmatch.R;
import com.example.outfitmatch.modelo.entidad.Prenda;

import java.util.List;

/**
 * AdaptadorTransition es un adaptador personalizado para un RecyclerView
 * que muestra una lista de prendas en una vista de transición tipo "Tinder".
 * Utiliza Glide para cargar imágenes desde una URL.
 */
public class AdaptadorTransition extends RecyclerView.Adapter<AdaptadorTransition.ViewHolder> {

    private List<Prenda> prendas;  // Lista de prendas a mostrar

    /**
     * Constructor para AdaptadorTransition.
     *
     * @param prendas Lista de objetos Prenda a mostrar en el RecyclerView.
     */
    public AdaptadorTransition(List<Prenda> prendas) {
        this.prendas = prendas;
    }

    /**
     * Infla el layout para cada ítem del RecyclerView.
     *
     * @param parent   El ViewGroup al que se añadirá la nueva vista.
     * @param viewType El tipo de vista (no utilizado en este caso).
     * @return Una nueva instancia de ViewHolder.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_prenda, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Asocia los datos de una prenda con las vistas del ViewHolder.
     *
     * @param holder   ViewHolder que representa el ítem.
     * @param position Posición del ítem en la lista.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Prenda prenda = prendas.get(position);

        // Cargar la imagen desde la URL usando Glide
        Glide.with(holder.itemView.getContext())
                .load(prenda.getImagenUrl())                  // Cargar desde la URL
                .into(holder.imgCardPrenda);
    }

    /**
     * Retorna el número total de elementos en la lista.
     *
     * @return Cantidad de ítems en el RecyclerView.
     */
    @Override
    public int getItemCount() {
        return prendas.size();
    }

    /**
     * ViewHolder representa cada ítem del RecyclerView.
     * Contiene referencias a las vistas que se mostrarán.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCardPrenda;  // Imagen de la prenda

        /**
         * Constructor del ViewHolder.
         *
         * @param itemView La vista del ítem.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCardPrenda = itemView.findViewById(R.id.imgCardPrenda);
        }
    }
}
