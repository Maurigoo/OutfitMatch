package com.outfitmatch;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class ClothingCategoryActivity extends AppCompatActivity {
    private String category;
    private RecyclerView recyclerView;
    private ClothingAdapter adapter; // Asegúrate de tener un adapter para las prendas.
    private List<ClothingItem> clothingItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothing_category);

        category = getIntent().getStringExtra("CATEGORY");
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ClothingAdapter(clothingItems);
        recyclerView.setAdapter(adapter);

        loadCategoryData();
    }

    private void loadCategoryData() {
        FirebaseFirestore.getInstance()
                .collection("clothing")
                .whereEqualTo("category", category)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    clothingItems.clear();
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        ClothingItem item = document.toObject(ClothingItem.class);
                        clothingItems.add(item);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al cargar prendas", Toast.LENGTH_SHORT).show();
                });
    }
}