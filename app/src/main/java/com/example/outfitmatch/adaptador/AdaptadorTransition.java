package com.example.outfitmatch.adaptador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.outfitmatch.R;
import com.example.outfitmatch.modelo.entidad.Prenda;

import java.util.List;

public class AdaptadorTransition extends RecyclerView.Adapter<AdaptadorTransition.ViewHolder> {

    private List<Prenda> prendas;

    public AdaptadorTransition(List<Prenda> prendas) {
        this.prendas = prendas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_prenda, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Prenda prenda = prendas.get(position);
        holder.imgCardPrenda.setImageResource(prenda.getImageResource());
    }

    @Override
    public int getItemCount() {
        return prendas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCardPrenda;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCardPrenda = itemView.findViewById(R.id.imgCardPrenda);
        }
    }
}
