package com.example.outfitmatch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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

    /**
     * Método llamado al crear la actividad. Configura la interfaz y carga los outfits.
     *
     * @param savedInstanceState Estado previamente guardado de la actividad (si aplica).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
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
        BottomNavigationView bottomNavigationView = findViewById(R.id.boton_navigation);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    startActivity(new Intent(getApplicationContext(), Home.class));
                } else if (itemId == R.id.nav_clothes) {
                    startActivity(new Intent(getApplicationContext(), Clothes.class));
                } else if (itemId == R.id.nav_add) {
                    startActivity(new Intent(getApplicationContext(), AddClothesAlbum.class));
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(getApplicationContext(), Perfil.class));
                }

                overridePendingTransition(0, 0);
                return true;
            }
        });
    }

    /**
     * Carga los outfits guardados en Firestore para el usuario actual.
     * Los datos se obtienen desde la colección 'saved_outfits' y se muestran en el RecyclerView.
     */
    private void loadSavedOutfitsFromFirestore() {
        db.collection("users").document(userId).collection("saved_outfits")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        savedOutfits.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Obtener datos de la prenda desde Firestore
                            String imagenUrl = document.getString("imagenUrl");
                            String talla = document.getString("talla");
                            String material = document.getString("material");
                            String color = document.getString("color");
                            String tipo = document.getString("tipo");

                            // Crear objeto Prenda
                            Prenda prenda = new Prenda(0, talla, material, color, tipo);
                            prenda.setImagenUrl(imagenUrl);

                            // Agregar prenda a la lista
                            savedOutfits.add(prenda);
                        }
                        // Actualizar RecyclerView
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Error al cargar outfits", Toast.LENGTH_SHORT).show();
                        Log.e("Firestore", "Error al obtener outfits", task.getException());
                    }
                });
    }
}
