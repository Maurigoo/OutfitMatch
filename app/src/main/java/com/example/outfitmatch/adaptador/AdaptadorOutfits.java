package com.example.outfitmatch.adaptador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.outfitmatch.R;
import com.example.outfitmatch.modelo.entidad.Prenda;

import java.util.List;

public class AdaptadorOutfits extends RecyclerView.Adapter<AdaptadorOutfits.OutfitViewHolder> {

    private List<Prenda> outfits;

    public AdaptadorOutfits(List<Prenda> outfits) {
        this.outfits = outfits;
    }

    @Override
    public OutfitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_outfit, parent, false);
        return new OutfitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OutfitViewHolder holder, int position) {
        Prenda prenda = outfits.get(position);
        holder.imageView.setImageResource(prenda.getImagen());
        holder.sizeTextView.setText("Tamaño: " + prenda.getTalla());
        holder.colorTextView.setText("Color: " + prenda.getColor());
    }

    @Override
    public int getItemCount() {
        return outfits.size();
    }

    public static class OutfitViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView sizeTextView, colorTextView;

        public OutfitViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgOutfit);
            sizeTextView = itemView.findViewById(R.id.textViewTalla);
            colorTextView = itemView.findViewById(R.id.textViewColor);
        }
    }
}