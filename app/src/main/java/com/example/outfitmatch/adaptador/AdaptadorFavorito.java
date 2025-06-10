package com.example.outfitmatch.adaptador;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

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

    // Constructor
    public AdaptadorFavorito(Context context, List<Prenda> prendasFavoritas, String userId, OnFavoritosEmptyListener listener) {
        this.context = context;
        this.prendasFavoritas = prendasFavoritas;
        this.userId = userId;
        this.listener = listener;

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

        Glide.with(context)
                .load(prenda.getImagenUrl())
                .into(holder.imgPrenda);

        holder.deleteButton.setEnabled(true);
        holder.deleteButton.setAlpha(1f);
        holder.deleteButton.setScaleX(1f);
        holder.deleteButton.setScaleY(1f);

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

            isDeleting = true;
            holder.deleteButton.setEnabled(false);

            Prenda prendaToDelete = prendasFavoritas.get(currentPosition);
            String prendaId = prendaToDelete.getId();

            if (prendaId == null || prendaId.isEmpty()) {
                Log.w("AdaptadorFavorito", "ID de prenda no válido");
                Toast.makeText(context, "ID de prenda no válido", Toast.LENGTH_SHORT).show();
                isDeleting = false;
                holder.deleteButton.setEnabled(true);
                return;
            }

            // Animación dramática: fade out + scale down
            AnimatorSet animatorSet = new AnimatorSet();

            ObjectAnimator fadeOut = ObjectAnimator.ofFloat(holder.deleteButton, "alpha", 1f, 0f);
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(holder.deleteButton, "scaleX", 1f, 0f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(holder.deleteButton, "scaleY", 1f, 0f);

            animatorSet.playTogether(fadeOut, scaleX, scaleY);
            animatorSet.setDuration(600);
            animatorSet.start();

            animatorSet.addListener(new android.animation.AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(android.animation.Animator animation) {
                    progressDialog.show();

                    long startTime = System.currentTimeMillis();
                    long MIN_DURATION = 2000;

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
                                    int pos = holder.getBindingAdapterPosition();
                                    if (pos == RecyclerView.NO_POSITION) pos = currentPosition;

                                    if (pos >= 0 && pos < prendasFavoritas.size()) {
                                        Prenda currentPrenda = prendasFavoritas.get(pos);
                                        if (currentPrenda.getId().equals(prendaId)) {
                                            prendasFavoritas.remove(pos);
                                            notifyItemRemoved(pos);
                                        }
                                    }

                                    if (prendasFavoritas.isEmpty() && listener != null) {
                                        listener.onFavoritosEmpty();
                                    }

                                    progressDialog.dismiss();
                                    isDeleting = false;

                                    View rootView = ((ViewGroup) ((ViewGroup) ((android.app.Activity) context)
                                            .findViewById(android.R.id.content)).getChildAt(0));
                                    Snackbar.make(rootView, "Prenda eliminada", Snackbar.LENGTH_SHORT).show();

                                }, remaining);
                            })
                            .addOnFailureListener(e -> {
                                progressDialog.dismiss();
                                Toast.makeText(context, "Error al eliminar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                isDeleting = false;
                                holder.deleteButton.setEnabled(true);
                                // Restaurar estado del botón en caso de error
                                holder.deleteButton.setAlpha(1f);
                                holder.deleteButton.setScaleX(1f);
                                holder.deleteButton.setScaleY(1f);
                            });
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return prendasFavoritas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPrenda;
        ImageView deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPrenda = itemView.findViewById(R.id.imgCardPrenda);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
