package com.example.outfitmatch.adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.outfitmatch.R;
import com.example.outfitmatch.modelo.entidad.Prenda;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AdaptadorFavorito extends RecyclerView.Adapter<AdaptadorFavorito.ViewHolder> {

    private List<Prenda> prendasFavoritas;
    private Context context;
    private String userId;

    public AdaptadorFavorito(Context context, List<Prenda> prendasFavoritas, String userId) {
        this.context = context;
        this.prendasFavoritas = prendasFavoritas;
        this.userId = userId;
    }

    @NonNull
    @Override
    public AdaptadorFavorito.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_prenda, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorFavorito.ViewHolder holder, int position) {
        Prenda prenda = prendasFavoritas.get(position);

        Glide.with(context)
                .load(prenda.getImagenUrl())
                .into(holder.imgPrenda);
        holder.deleteButton.setOnClickListener(view -> {
            String prendaId = prenda.getId();
            if (prendaId == null || prendaId.isEmpty()) {
                Toast.makeText(context, "ID de prenda no válido", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(userId)
                    .collection("favoritos")
                    .document(prendaId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        prendasFavoritas.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(context, "Prenda eliminada de favoritos", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Error al eliminar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

    }


    @Override
    public int getItemCount() {
        return prendasFavoritas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPrenda;
        ImageView deleteButton; // Agregado el botón de eliminar

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPrenda = itemView.findViewById(R.id.imgCardPrenda);
            deleteButton = itemView.findViewById(R.id.deleteButton); // Reemplaza con el ID real del botón
        }
    }
}
