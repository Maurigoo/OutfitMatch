package com.example.outfitmatch;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.outfitmatch.adaptador.AdaptadorOutfits;
import com.example.outfitmatch.modelo.entidad.Prenda;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class Outfits extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdaptadorOutfits adapter;
    private List<Prenda> savedOutfits = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outfits);

        recyclerView = findViewById(R.id.recyclerViewClothes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Recibir los outfits guardados desde la actividad anterior
        Intent intent = getIntent();
        if (intent != null) {
            savedOutfits = (List<Prenda>) intent.getSerializableExtra("savedOutfits");
            if (savedOutfits == null) {
                savedOutfits = new ArrayList<>();
            }
        }

        // Configurar el adaptador con los outfits guardados
        adapter = new AdaptadorOutfits(savedOutfits);
        recyclerView.setAdapter(adapter);

        // Configurar BottomNavigationView
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
}