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

public class CardStackAdapter<P> extends RecyclerView.Adapter<CardStackAdapter.ViewHolder> {

    private List<Prenda> outfitList;

    public CardStackAdapter(List<Prenda> outfitList) {
        this.outfitList = outfitList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Prenda outfit = outfitList.get(position);

        // Configurar la imagen
        Glide.with(holder.itemView.getContext())
                .load(outfit.getImagenUrl())
                .into(holder.imgOutfit);

        // Configurar los textos
        holder.textViewTalla.setText("Talla: " + outfit.getTalla());
        holder.textViewMaterial.setText("Material: " + outfit.getMaterial());
        holder.textViewColor.setText("Color: " + outfit.getColor());
    }

    @Override
    public int getItemCount() {
        return outfitList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgOutfit;
        TextView textViewTalla, textViewMaterial, textViewColor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgOutfit = itemView.findViewById(R.id.imgOutfit);
            textViewTalla = itemView.findViewById(R.id.textViewTalla);
            textViewMaterial = itemView.findViewById(R.id.textViewMaterial);
            textViewColor = itemView.findViewById(R.id.textViewColor);
        }
    }
}
