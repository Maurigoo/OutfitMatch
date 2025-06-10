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

public class AdaptadorClothes extends RecyclerView.Adapter<AdaptadorClothes.ViewHolder> {

    private List<Prenda> prendas;
    private OnItemClickListener listener;
    private OnItemLongClickListener longClickListener;

    public AdaptadorClothes(List<Prenda> prendas, OnItemClickListener listener, OnItemLongClickListener longClickListener) {
        this.prendas = prendas;
        this.listener = listener;
        this.longClickListener = longClickListener;
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

        Log.d("AdaptadorClothes", "URL de imagen: " + prenda.getImagenUrl());

        Glide.with(holder.itemView.getContext())
                .load(prenda.getImagenUrl())
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(holder.imageViewClothes);

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

        holder.itemView.setOnClickListener(v -> listener.onItemClick(prenda));

        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Eliminar prenda")
                    .setMessage("¿Estás segura de que deseas eliminar esta prenda?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        // Aquí notificamos a la actividad para eliminar de Firestore + UI
                        longClickListener.onItemLongClick(prenda, position);
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
            return true;
        });
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

    public interface OnItemLongClickListener {
        void onItemLongClick(Prenda prenda, int position);
    }
}
