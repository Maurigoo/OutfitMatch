package com.example.outfitmatch.adaptador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.outfitmatch.R;
import com.example.outfitmatch.modelo.entidad.Prenda;

import java.util.List;

/**
 * AdaptadorOutfits es un adaptador personalizado para un RecyclerView
 * que muestra una lista de prendas (outfits) usando una vista tipo CardView.
 * Utiliza Glide para cargar imágenes desde una URL.
 */
public class AdaptadorOutfits extends RecyclerView.Adapter<AdaptadorOutfits.OutfitViewHolder> {

    private List<Prenda> outfits;  // Lista de prendas a mostrar

    /**
     * Constructor para AdaptadorOutfits.
     *
     * @param outfits Lista de objetos Prenda a mostrar en el RecyclerView.
     */
    public AdaptadorOutfits(List<Prenda> outfits) {
        this.outfits = outfits;
    }

    /**
     * Infla el layout para cada ítem del RecyclerView.
     *
     * @param parent   El ViewGroup al que se añadirá la nueva vista.
     * @param viewType El tipo de vista (no utilizado en este caso).
     * @return Una nueva instancia de OutfitViewHolder.
     */
    @NonNull
    @Override
    public OutfitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_outfit, parent, false);
        return new OutfitViewHolder(view);
    }

    /**
     * Asocia los datos de una prenda con las vistas del ViewHolder.
     *
     * @param holder   ViewHolder que representa el ítem.
     * @param position Posición del ítem en la lista.
     */
    @Override
    public void onBindViewHolder(@NonNull OutfitViewHolder holder, int position) {
        Prenda prenda = outfits.get(position);

        // Cargar la imagen desde la URL usando Glide
        Glide.with(holder.itemView.getContext())
                .load(prenda.getImagenUrl())  // Cargar desde la URL
                .into(holder.imageView);

        // Mostrar detalles de la prenda en los TextViews
        holder.sizeTextView.setText("Tamaño: " + prenda.getTalla());
        holder.colorTextView.setText("Color: " + prenda.getColor());
        holder.materialTextView.setText("Material: " + prenda.getMaterial());
    }

    /**
     * Retorna el número total de elementos en la lista.
     *
     * @return Cantidad de ítems en el RecyclerView.
     */
    @Override
    public int getItemCount() {
        return outfits.size();
    }

    /**
     * ViewHolder representa cada ítem del RecyclerView.
     * Contiene referencias a las vistas que se mostrarán.
     */
    public static class OutfitViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;           // Imagen de la prenda
        TextView sizeTextView;         // Texto para la talla
        TextView colorTextView;        // Texto para el color
        TextView materialTextView;     // Texto para el material

        /**
         * Constructor del ViewHolder.
         *
         * @param itemView La vista del ítem.
         */
        public OutfitViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgOutfit);
            sizeTextView = itemView.findViewById(R.id.textViewTalla);
            colorTextView = itemView.findViewById(R.id.textViewColor);
            materialTextView = itemView.findViewById(R.id.textViewMaterial);
        }
    }
}
