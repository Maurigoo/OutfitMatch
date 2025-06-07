package com.example.outfitmatch;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.outfitmatch.adaptador.AdaptadorFavorito;
import com.example.outfitmatch.modelo.entidad.Prenda;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import me.ibrahimsn.lib.SmoothBottomBar;

public class Favorito extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdaptadorFavorito adapter;
    private List<Prenda> savedOutfits = new ArrayList<>();

    private FirebaseFirestore db;
    private String userId;

    private SmoothBottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.setStatusBarColor(Color.TRANSPARENT);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorito);

        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        recyclerView = findViewById(R.id.recyclerViewClothes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AdaptadorFavorito(savedOutfits);
        recyclerView.setAdapter(adapter);

        loadSavedOutfitsFromFirestore();

        bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setOnItemSelectedListener(new Function1<Integer, Unit>() {
            @Override
            public Unit invoke(Integer index) {
                if (index == 4) return Unit.INSTANCE;

                Class<?> destination = null;
                switch (index) {
                    case 0:
                        destination = Home.class;
                        break;
                    case 1:
                        destination = Clothes.class;
                        break;
                    case 2:
                        destination = AddClothesAlbum.class;
                        break;
                    case 3:
                        destination = Perfil.class;
                        break;
                    case 4:
                        destination = GenerarOutfit.class;
                        break;
                }

                if (destination != null) {
                    startActivity(new Intent(getApplicationContext(), destination));
                    overridePendingTransition(0, 0);
                }
                return Unit.INSTANCE;
            }
        });
    }

    private void loadSavedOutfitsFromFirestore() {
        db.collection("users").document(userId).collection("favoritos")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        savedOutfits.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String imagenUrl = document.getString("imagenUrl");
                            String talla = document.getString("talla");
                            String material = document.getString("material");
                            String color = document.getString("color");
                            String tipo = document.getString("tipo");

                            Prenda prenda = new Prenda(0, talla, material, color, tipo);
                            prenda.setImagenUrl(imagenUrl);

                            savedOutfits.add(prenda);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Error al cargar favoritos", Toast.LENGTH_SHORT).show();
                        Log.e("Firestore", "Error al obtener favoritos", task.getException());
                    }
                });
    }
}
