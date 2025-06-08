package com.example.outfitmatch.adaptador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.outfitmatch.R;
import com.example.outfitmatch.modelo.entidad.Prenda;

import android.widget.ImageView;

import java.util.List;

public class AdaptadorTransition extends RecyclerView.Adapter<AdaptadorTransition.ViewHolder> {

    private List<List<Prenda>> outfits;

    public AdaptadorTransition(@NonNull List<List<Prenda>> outfits) {
        this.outfits = outfits;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        List<Prenda> outfit = outfits.get(position);

        // Reiniciar imágenes para evitar errores de reciclaje
        holder.imagenTop.setImageDrawable(null);
        holder.imagenPant.setImageDrawable(null);
        holder.imagenShoe.setImageDrawable(null);

        // Cargar imágenes según el tamaño del outfit
        if (outfit.size() > 0) {
            Glide.with(holder.itemView.getContext())
                    .load(outfit.get(0).getImagenUrl())
                    .into(holder.imagenTop); // Primera prenda
        }

        if (outfit.size() > 1) {
            Glide.with(holder.itemView.getContext())
                    .load(outfit.get(1).getImagenUrl())
                    .into(holder.imagenPant); // Segunda prenda
        }

        if (outfit.size() > 2) {
            Glide.with(holder.itemView.getContext())
                    .load(outfit.get(2).getImagenUrl())
                    .into(holder.imagenShoe); // Tercera prenda
        }
    }

    @Override
    public int getItemCount() {
        return outfits.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imagenTop;
        ImageView imagenPant;
        ImageView imagenShoe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenTop = itemView.findViewById(R.id.imgTop);
            imagenPant = itemView.findViewById(R.id.imgPant);
            imagenShoe = itemView.findViewById(R.id.imgShoe);
        }
    }
}
