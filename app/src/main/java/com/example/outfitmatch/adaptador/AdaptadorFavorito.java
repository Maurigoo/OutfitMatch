package com.example.outfitmatch.adaptador;

import android.app.ProgressDialog;
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

    private final List<Prenda> prendasFavoritas;
    private final Context context;
    private final String userId;
    private final ProgressDialog progressDialog;
    private final OnFavoritosEmptyListener listener;

    // Constructor único
    public AdaptadorFavorito(Context context, List<Prenda> prendasFavoritas, String userId, OnFavoritosEmptyListener listener) {
        this.context = context;
        this.prendasFavoritas = prendasFavoritas;
        this.userId = userId;
        this.listener = listener;

        // Inicializar ProgressDialog
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Eliminando prenda...");
        progressDialog.setCancelable(false);
    }

    // Interfaz para notificar que la lista está vacía
    public interface OnFavoritosEmptyListener {
        void onFavoritosEmpty();
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

        // Cargar imagen con Glide
        Glide.with(context)
                .load(prenda.getImagenUrl())
                .into(holder.imgPrenda);

        // Configurar el botón de eliminar
        holder.deleteButton.setOnClickListener(view -> {
            String prendaId = prenda.getId();
            if (prendaId == null || prendaId.isEmpty()) {
                Toast.makeText(context, "ID de prenda no válido", Toast.LENGTH_SHORT).show();
                return;
            }

            progressDialog.show();

            FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(userId)
                    .collection("favoritos")
                    .document(prendaId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        prendasFavoritas.remove(position);
                        notifyItemRemoved(position);

                        // Verificar si la lista está vacía
                        if (prendasFavoritas.isEmpty()) {
                            if (listener != null) {
                                listener.onFavoritosEmpty(); // Notificar a la actividad o fragmento
                            }
                        }

                        progressDialog.dismiss();
                        Toast.makeText(context, "Prenda eliminada de favoritos", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
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
        ImageView deleteButton; // Botón de eliminar

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPrenda = itemView.findViewById(R.id.imgCardPrenda);
            deleteButton = itemView.findViewById(R.id.deleteButton); // ID del botón de eliminar
        }
    }
}
