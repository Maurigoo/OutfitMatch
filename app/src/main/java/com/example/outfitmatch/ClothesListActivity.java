package com.example.outfitmatch;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.outfitmatch.adaptador.AdaptadorClothes;
import com.example.outfitmatch.modelo.entidad.Prenda;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ClothesListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdaptadorClothes clothesAdapter;
    private List<Prenda> clothesList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes_list);

        recyclerView = findViewById(R.id.recyclerViewClothes);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        clothesList = new ArrayList<>();
        clothesAdapter = new AdaptadorClothes(clothesList, prenda ->
                Toast.makeText(this, "Seleccionaste: " + prenda.getTalla(), Toast.LENGTH_SHORT).show()
        );
        recyclerView.setAdapter(clothesAdapter);

        db = FirebaseFirestore.getInstance();

        // Obtener la categoría seleccionada desde el Intent
        String category = getIntent().getStringExtra("CATEGORY");

        if (category != null) {
            loadClothesFromFirestore(category);
        } else {
            Toast.makeText(this, "No category selected", Toast.LENGTH_SHORT).show();
        }
    }
    private void loadClothesFromFirestore(String category) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("prendas").document(userId).collection("user_prendas")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        clothesList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String tipo = document.getString("tipo");
                            if (category.equals("All") || category.equals(tipo)) {
                                String imagenUrl = document.getString("imagen");
                                String talla = document.getString("talla");
                                String material = document.getString("material");
                                String color = document.getString("color");

                                Prenda prenda = new Prenda(0, talla, material, color, tipo);
                                prenda.setImagenUrl(imagenUrl); // Asegúrate de tener este campo en Prenda
                                clothesList.add(prenda);
                            }
                        }
                        clothesAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Error al cargar prendas", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
