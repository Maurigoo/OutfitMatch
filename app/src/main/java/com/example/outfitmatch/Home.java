package com.example.outfitmatch;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends AppCompatActivity {

    Button articles, ideas, outfit;
    ImageView gifImageView; // Declara el ImageView para el GIF

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Inicializa el ImageView para el GIF
        gifImageView = findViewById(R.id.gifImageView);

        // Cargar el GIF con Glide para que se reproduzca infinitamente
        Glide.with(this)
                .load(R.drawable.hanger_animation) // Asegúrate de que el GIF esté en la carpeta res/drawable
                .into(gifImageView);

        BottomNavigationView bottomNavigationView = findViewById(R.id.boton_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    return true;
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

        // Botones para las otras actividades
        articles = findViewById(R.id.botonArticles);
        ideas = findViewById(R.id.botonIdeas);
        outfit = findViewById(R.id.botonOufits);

        articles.setOnClickListener(view -> {
            Intent intent = new Intent(Home.this, Clothes.class);
            startActivity(intent);
        });

        ideas.setOnClickListener(view -> {
            Intent intent = new Intent(Home.this, Transition.class);
            startActivity(intent);
        });

        outfit.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, Outfits.class);
            startActivity(intent);
        });
    }
}
