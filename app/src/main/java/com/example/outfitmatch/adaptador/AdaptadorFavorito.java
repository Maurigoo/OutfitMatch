package com.example.outfitmatch.adaptador;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
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
    private boolean isDeleting = false;

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
        Prenda prenda = prendasFavoritas.get(holder.getBindingAdapterPosition());

        Glide.with(context)
                .load(prenda.getImagenUrl())
                .into(holder.imgPrenda);

        holder.deleteButton.setOnClickListener(view -> {
            if (isDeleting) {
                Log.d("AdaptadorFavorito", "Ignorando click porque ya hay una eliminación en curso");
                return;
            }

            int currentPosition = holder.getBindingAdapterPosition();
            if (currentPosition == RecyclerView.NO_POSITION) {
                Log.w("AdaptadorFavorito", "Posición inválida, no se eliminará nada.");
                return;
            }

            isDeleting = true; // Bloqueamos más clicks hasta acabar
            Log.d("AdaptadorFavorito", "Click en eliminar. Posición actual: " + currentPosition);

            String prendaId = prenda.getId();
            if (prendaId == null || prendaId.isEmpty()) {
                Log.w("AdaptadorFavorito", "ID de prenda no válido");
                Toast.makeText(context, "ID de prenda no válido", Toast.LENGTH_SHORT).show();
                isDeleting = false;
                return;
            }

            progressDialog.show();

            long startTime = System.currentTimeMillis();
            long MIN_DURATION = 2000; // duración mínima en milisegundos (2 segundos)

            FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(userId)
                    .collection("favoritos")
                    .document(prendaId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        long elapsed = System.currentTimeMillis() - startTime;
                        long remaining = MIN_DURATION - elapsed;
                        if (remaining < 0) remaining = 0;

                        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                            Log.d("AdaptadorFavorito", "Eliminado en Firestore prenda con posición: " + currentPosition);
                            if (currentPosition >= 0 && currentPosition < prendasFavoritas.size()) {
                                prendasFavoritas.remove(currentPosition);
                                notifyItemRemoved(currentPosition);
                                Log.d("AdaptadorFavorito", "Eliminado localmente prenda en posición: " + currentPosition);
                            } else {
                                Log.w("AdaptadorFavorito", "Posición fuera de rango para eliminar localmente: " + currentPosition);
                            }

                            if (prendasFavoritas.isEmpty() && listener != null) {
                                listener.onFavoritosEmpty();
                            }

                            progressDialog.dismiss();
                            isDeleting = false; // Desbloqueamos clicks
                        }, remaining);
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Log.e("AdaptadorFavorito", "Error al eliminar: " + e.getMessage());
                        Toast.makeText(context, "Error al eliminar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        isDeleting = false; // Desbloqueamos clicks aunque haya error
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
