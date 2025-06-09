package com.example.outfitmatch.adaptador;

import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.outfitmatch.R;
import com.example.outfitmatch.modelo.entidad.Prenda;

import java.util.List;

/**
 * AdaptadorClothes es un adaptador personalizado para un RecyclerView que muestra
 * una lista de prendas. Utiliza Glide para cargar imágenes desde una URL.
 */
public class AdaptadorClothes extends RecyclerView.Adapter<AdaptadorClothes.ViewHolder> {

    private List<Prenda> prendas;                // Lista de prendas a mostrar
    private OnItemClickListener listener;        // Listener para manejar clics en los ítems
    private OnItemLongClickListener longClickListener; // Listener para manejar pulsación larga

    /**
     * Constructor para AdaptadorClothes.
     *
     * @param prendas          Lista de objetos Prenda a mostrar en el RecyclerView.
     * @param listener         Listener para manejar eventos de clic en los ítems.
     * @param longClickListener Listener para manejar eventos de mantener pulsado.
     */
    public AdaptadorClothes(List<Prenda> prendas, OnItemClickListener listener, OnItemLongClickListener longClickListener) {
        this.prendas = prendas;
        this.listener = listener;
        this.longClickListener = longClickListener;
    }

    /**
     * Infla el layout de cada ítem del RecyclerView.
     *
     * @param parent   El ViewGroup al que se añadirá la nueva vista.
     * @param viewType El tipo de vista (no utilizado en este caso).
     * @return Una nueva instancia de ViewHolder.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_clothes, parent, false);
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

        // Log para verificar la URL de la imagen
        Log.d("AdaptadorClothes", "URL de imagen: " + prenda.getImagenUrl());

        // Cargar imagen desde la URL usando Glide
        Glide.with(holder.itemView.getContext())
                .load(prenda.getImagenUrl())  // Asegúrate que esta URL sea válida
                .placeholder(R.drawable.placeholder_image) // Imagen por defecto si carga es lenta
                .error(R.drawable.error_image)             // Imagen por defecto si falla la carga
                .into(holder.imageViewClothes);

        // Verificar y mostrar valores con fallback a "No especificado"
        String talla = (prenda.getTalla() != null && !prenda.getTalla().isEmpty())
                ? prenda.getTalla()
                : "No especificado";
        holder.textViewTalla.setText("Talla: " + talla);

        String material = (prenda.getMaterial() != null && !prenda.getMaterial().isEmpty())
                ? prenda.getMaterial()
                : "No especificado";
        holder.textViewMateriales.setText("Material: " + material);

        String color = (prenda.getColor() != null && !prenda.getColor().isEmpty())
                ? prenda.getColor()
                : "No especificado";
        holder.textViewColor.setText("Color: " + color);

        // Manejar clic en el ítem
        holder.itemView.setOnClickListener(v -> listener.onItemClick(prenda));

        // Manejar pulsación larga en el ítem
        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Eliminar prenda")
                    .setMessage("¿Estás segura de que deseas eliminar esta prenda?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        longClickListener.onItemLongClick(prenda, position);
                        Toast.makeText(holder.itemView.getContext(), "Prenda eliminada", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
            return true;
        });
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
        public ImageView imageViewClothes;       // Imagen de la prenda
        public TextView textViewTalla;           // Texto para la talla
        public TextView textViewMateriales;      // Texto para el material
        public TextView textViewColor;           // Texto para el color

        /**
         * Constructor del ViewHolder.
         *
         * @param itemView La vista del ítem.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewClothes = itemView.findViewById(R.id.imageViewClothes);
            textViewTalla = itemView.findViewById(R.id.textViewTalla);
            textViewMateriales = itemView.findViewById(R.id.textViewMateriales);
            textViewColor = itemView.findViewById(R.id.textViewColor);
        }
    }

    /**
     * Interfaz para manejar eventos de clic en los ítems del RecyclerView.
     */
    public interface OnItemClickListener {
        void onItemClick(Prenda prenda);
    }

    /**
     * Interfaz para manejar eventos de pulsación larga en los ítems del RecyclerView.
     */
    public interface OnItemLongClickListener {
        void onItemLongClick(Prenda prenda, int position);
    }
}
