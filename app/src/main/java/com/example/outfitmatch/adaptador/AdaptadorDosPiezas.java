package com.example.outfitmatch.adaptador;

import android.content.Context;
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

public class AdaptadorDosPiezas extends RecyclerView.Adapter<AdaptadorDosPiezas.ViewHolder> {

    private final List<Prenda> listaPrendas;
    private final OnPrendaSelectedListener listener;
    private final Context context;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public interface OnPrendaSelectedListener {
        void onPrendaSelected(Prenda prenda);
    }

    public AdaptadorDosPiezas(Context context, List<Prenda> listaPrendas, OnPrendaSelectedListener listener) {
        this.context = context;
        this.listaPrendas = listaPrendas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_adaptador_dos_piezas, parent, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Prenda prenda = listaPrendas.get(position);

        Glide.with(context)
                .load(prenda.getImagenUrl())
                .into(holder.imgPrenda);

        // Mostrar borde si está seleccionada
        if (position == selectedPosition) {
            holder.imgPrenda.setBackgroundResource(R.drawable.borde_seleccionado); // crea este drawable
        } else {
            holder.imgPrenda.setBackgroundResource(0);
        }

        holder.imgPrenda.setOnClickListener(v -> {
            int prevSelected = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(prevSelected);
            notifyItemChanged(selectedPosition);
            listener.onPrendaSelected(prenda);
        });
    }

    @Override
    public int getItemCount() {
        return listaPrendas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPrenda;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPrenda = itemView.findViewById(R.id.imgPrendaa);
        }
    }
}
