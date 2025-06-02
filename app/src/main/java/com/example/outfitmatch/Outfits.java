package com.example.outfitmatch;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.outfitmatch.adaptador.AdaptadorOutfits;
import com.example.outfitmatch.modelo.entidad.Prenda;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import me.ibrahimsn.lib.SmoothBottomBar;

/**
 * La clase Outfits muestra una lista de outfits guardados por el usuario en Firestore.
 * Los outfits seleccionados desde la actividad Transition se cargan aquí y se muestran
 * usando un RecyclerView.
 */
public class Outfits extends AppCompatActivity {

    private RecyclerView recyclerView;                   // Vista para mostrar los outfits
    private AdaptadorOutfits adapter;                    // Adaptador para los outfits
    private List<Prenda> savedOutfits = new ArrayList<>(); // Lista de prendas guardadas

    private FirebaseFirestore db;                        // Instancia de Firestore
    private String userId;                               // ID del usuario autenticado

    private SmoothBottomBar bottomBar;
    /**
     * Método llamado al crear la actividad. Configura la interfaz y carga los outfits.
     *
     * @param savedInstanceState Estado previamente guardado de la actividad (si aplica).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.setStatusBarColor(Color.TRANSPARENT);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outfits);

        // Inicialización de Firestore y autenticación
        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Configurar RecyclerView para mostrar outfits
        recyclerView = findViewById(R.id.recyclerViewClothes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar adaptador y asignarlo al RecyclerView
        adapter = new AdaptadorOutfits(savedOutfits);
        recyclerView.setAdapter(adapter);

        // Cargar los outfits guardados desde Firestore
        loadSavedOutfitsFromFirestore();

        // Configurar la barra de navegación inferior
        bottomBar = findViewById(R.id.bottomBar);

        bottomBar.setOnItemSelectedListener(new Function1<Integer, Unit>() {
            @Override
            public Unit invoke(Integer index) {
                if (index == 4) return Unit.INSTANCE; // Ya estás en esta pestaña

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

                return Unit.INSTANCE; // Kotlin's void
            }
        });

    }

    /**
     * Carga los outfits guardados en Firestore para el usuario actual.
     * Los datos se obtienen desde la colección 'saved_outfits' y se muestran en el RecyclerView.
     */
    private void loadSavedOutfitsFromFirestore() {
        db.collection("users").document(userId)
                .collection("current_outfit").document("outfit_actual")
                .get()
                .addOnSuccessListener(document -> {
                    savedOutfits.clear();

                    if (document.exists()) {
                        List<Map<String, Object>> prendasMap = (List<Map<String, Object>>) document.get("prendas");

                        if (prendasMap != null) {
                            for (Map<String, Object> prendaMap : prendasMap) {
                                String imagenUrl = (String) prendaMap.get("imagenUrl");
                                String talla = (String) prendaMap.get("talla");
                                String material = (String) prendaMap.get("material");
                                String color = (String) prendaMap.get("color");
                                String tipo = (String) prendaMap.get("tipo");

                                Prenda prenda = new Prenda(0, talla, material, color, tipo);
                                prenda.setImagenUrl(imagenUrl);
                                savedOutfits.add(prenda);
                            }
                        }
                    }

                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al cargar el outfit actual", Toast.LENGTH_SHORT).show();
                    Log.e("Firestore", "Error al obtener outfit actual", e);
                });
    }

}
