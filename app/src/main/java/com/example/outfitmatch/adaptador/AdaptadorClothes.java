package com.example.outfitmatch.adaptador;

import android.util.Log;
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

public class AdaptadorClothes extends RecyclerView.Adapter<AdaptadorClothes.ViewHolder> {

    private List<Prenda> prendas;
    private OnItemClickListener listener;

    public AdaptadorClothes(List<Prenda> prendas, OnItemClickListener listener) {
        this.prendas = prendas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_clothes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Prenda prenda = prendas.get(position);

        // Log para verificar la URL de la imagen
        Log.d("AdaptadorClothes", "URL de imagen: " + prenda.getImagenUrl());

        // Cargar imagen desde la URL usando Glide
        Glide.with(holder.itemView.getContext())
                .load(prenda.getImagenUrl())  // Asegúrate que esta URL sea válida
                .into(holder.imageViewClothes);

        // Cargar el resto de los datos
        holder.textViewTalla.setText("Talla: " + prenda.getTalla());
        holder.textViewMateriales.setText("Material: " + prenda.getMaterial());
        holder.textViewColor.setText("Color: " + prenda.getColor());

        // Manejar clic en el item
        holder.itemView.setOnClickListener(v -> listener.onItemClick(prenda));
    }


    @Override
    public int getItemCount() {
        return prendas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewClothes;
        public TextView textViewTalla;
        public TextView textViewMateriales;
        public TextView textViewColor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewClothes = itemView.findViewById(R.id.imageViewClothes);
            textViewTalla = itemView.findViewById(R.id.textViewTalla);
            textViewMateriales = itemView.findViewById(R.id.textViewMateriales);
            textViewColor = itemView.findViewById(R.id.textViewColor);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Prenda prenda);
    }
}