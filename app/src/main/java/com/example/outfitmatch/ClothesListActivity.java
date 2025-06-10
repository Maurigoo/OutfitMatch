package com.example.outfitmatch;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.outfitmatch.adaptador.AdaptadorClothes;
import com.example.outfitmatch.modelo.entidad.Prenda;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import android.net.Uri;


public class ClothesListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdaptadorClothes clothesAdapter;
    private List<Prenda> clothesList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.applyTheme(this);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.setStatusBarColor(Color.TRANSPARENT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes_list);

        recyclerView = findViewById(R.id.recyclerViewClothes);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // Dos columnas

        clothesList = new ArrayList<>();
        clothesAdapter = new AdaptadorClothes(clothesList,
                prenda -> Toast.makeText(this, "Seleccionaste: " + prenda.getTalla(), Toast.LENGTH_SHORT).show(),
                (prenda, position) -> {
                    clothesList.remove(position);
                    clothesAdapter.notifyItemRemoved(position);
                    eliminarPrendaDeFirestore(prenda);
                    Toast.makeText(this, "Prenda eliminada", Toast.LENGTH_SHORT).show();
                }
        );
        recyclerView.setAdapter(clothesAdapter);
        db = FirebaseFirestore.getInstance();

        String category = getIntent().getStringExtra("CATEGORY");
        if (category != null) {
            loadClothesFromFirestore(category);
        } else {
            Toast.makeText(this, "No category selected", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadClothesFromFirestore(String category) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("prendas").document(userId).collection("user_prendas")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        clothesList.clear();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String tipo = document.getString("tipo");

                            if ("Dresses".equalsIgnoreCase(tipo)) {
                                tipo = "Jackets";
                            }

                            if (category.equals("All") || category.equalsIgnoreCase(tipo)) {
                                String imagenUrl = document.getString("imagenUrl");

                                if (document.contains("imagen")) {
                                    Object imagenObj = document.get("imagen");

                                    if (imagenObj instanceof String) {
                                        imagenUrl = (String) imagenObj;
                                    } else if (imagenObj instanceof Map) {
                                        Map<?, ?> imagenMap = (Map<?, ?>) imagenObj;
                                        Object urlObj = imagenMap.get("imagenUrl");
                                        if (urlObj instanceof String) {
                                            imagenUrl = (String) urlObj;
                                        }
                                    } else if (imagenObj instanceof List) {
                                        List<?> imagenList = (List<?>) imagenObj;
                                        if (!imagenList.isEmpty() && imagenList.get(0) instanceof String) {
                                            imagenUrl = (String) imagenList.get(0);
                                        }
                                    }
                                }

                                String talla = document.getString("talla");
                                String material = document.getString("material");
                                String color = document.getString("color");

                                Prenda prenda = new Prenda(0, talla, material, color, tipo);
                                if (imagenUrl != null) {
                                    prenda.setImagenUrl(imagenUrl);
                                }

                                // Aquí guardamos el ID del documento
                                prenda.setId(document.getId());

                                clothesList.add(prenda);
                            }
                        }

                        clothesAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Error al cargar prendas", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void eliminarPrendaDeFirestore(Prenda prenda) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (prenda.getId() != null) {
            // 1. Eliminar documento de Firestore
            db.collection("prendas").document(userId)
                    .collection("user_prendas").document(prenda.getId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Log.d("Firestore", "Prenda eliminada correctamente");

                        // 2. Eliminar imagen de Firebase Storage (si hay URL)
                        if (prenda.getImagenUrl() != null) {
                            eliminarImagenDeStorage(prenda.getImagenUrl());
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firestore", "Error eliminando prenda", e);
                        Toast.makeText(this, "Error al eliminar prenda de la base de datos", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "No se pudo identificar la prenda para eliminar", Toast.LENGTH_SHORT).show();
        }
    }

    private void eliminarImagenDeStorage(String imageUrl) {
        try {
            StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
            storageRef.delete()
                    .addOnSuccessListener(aVoid -> Log.d("Storage", "Imagen eliminada de Firebase Storage"))
                    .addOnFailureListener(e -> Log.e("Storage", "Error al eliminar imagen", e));
        } catch (Exception e) {
            Log.e("Storage", "URL inválida o error eliminando imagen", e);
        }
    }

}
