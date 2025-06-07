package com.outfitmatch;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.outfitmatch.R;
import com.example.outfitmatch.modelo.entidad.Outfit;
import com.example.outfitmatch.modelo.entidad.Prenda;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class GeneradorIdeas extends AppCompatActivity {
    private List<Prenda> shirts = new ArrayList<>();
    private List<Prenda> pants = new ArrayList<>();
    private List<Prenda> shoes = new ArrayList<>();
    private Prenda currentOutfit;

    private RecyclerView recyclerView;
    private OutfitAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generador_ideas);

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new OutfitAdapter();
        recyclerView.setAdapter(adapter);

        Button generateButton = findViewById(R.id.generateButton);
        generateButton.setOnClickListener(v -> generateOutfit());

        loadAllClothingItems();
    }

    private void loadAllClothingItems() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("clothing")
                .whereEqualTo("category", "shirt")
                .get().addOnSuccessListener(querySnapshot -> {
                    for (var document : querySnapshot) {
                        shirts.add(document.toObject(ClothingItem.class));
                    }
                });

        db.collection("clothing")
                .whereEqualTo("category", "pant")
                .get().addOnSuccessListener(querySnapshot -> {
                    for (var document : querySnapshot) {
                        pants.add(document.toObject(ClothingItem.class));
                    }
                });

        db.collection("clothing")
                .whereEqualTo("category", "shoe")
                .get().addOnSuccessListener(querySnapshot -> {
                    for (var document : querySnapshot) {
                        shoes.add(document.toObject(ClothingItem.class));
                    }
                });
    }

    private void generateOutfit() {
        if (shirts.isEmpty() || pants.isEmpty() || shoes.isEmpty()) {
            Toast.makeText(this, "Cargando prendas...", Toast.LENGTH_SHORT).show();
            return;
        }

        currentOutfit = OutfitGenerator.generateOutfit(shirts, pants, shoes);
        adapter.setOutfit(currentOutfit);
    }
}