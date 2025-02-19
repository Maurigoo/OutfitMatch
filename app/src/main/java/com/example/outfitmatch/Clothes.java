package com.example.outfitmatch;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Clothes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes);

        // Cargar el GIF usando Glide
        ImageView gifImageView = findViewById(R.id.gifImageView);  // El ImageView donde mostrarás el GIF
        Glide.with(this)
                .load(R.drawable.hanger_animation)  // Reemplaza con tu archivo GIF
                .into(gifImageView);

        ImageButton botonShirts = findViewById(R.id.botonShirts);
        ImageButton botonPants = findViewById(R.id.botonPants);
        ImageButton botonShoes = findViewById(R.id.botonShoes);
        ImageButton botonDresses = findViewById(R.id.botonDresses);
        ImageButton botonAccessories = findViewById(R.id.botonAccessories);
        ImageButton botonAll = findViewById(R.id.botonAll);

        // Listener para cada botón
        botonShirts.setOnClickListener(view -> openClothesListActivity("Shirts"));
        botonPants.setOnClickListener(view -> openClothesListActivity("Pants"));
        botonShoes.setOnClickListener(view -> openClothesListActivity("Shoes"));
        botonDresses.setOnClickListener(view -> openClothesListActivity("Dresses"));
        botonAccessories.setOnClickListener(view -> openClothesListActivity("Accessories"));
        botonAll.setOnClickListener(view -> openClothesListActivity("All"));

        BottomNavigationView bottomNavigationView = findViewById(R.id.boton_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_clothes);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    startActivity(new Intent(getApplicationContext(), Home.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.nav_clothes) {
                    startActivity(new Intent(getApplicationContext(), Clothes.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.nav_add) {
                    startActivity(new Intent(getApplicationContext(), AddClothesAlbum.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(getApplicationContext(), Perfil.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                return false;
            }
        });
    }

    // Método para abrir la actividad de lista de ropa
    private void openClothesListActivity(String category) {
        Intent intent = new Intent(Clothes.this, ClothesListActivity.class);
        intent.putExtra("CATEGORY", category);  // Pasar la categoría seleccionada
        startActivity(intent);
    }
}