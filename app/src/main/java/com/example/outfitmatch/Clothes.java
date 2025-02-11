package com.example.outfitmatch;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Clothes extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes);


        BottomNavigationView bottomNavigationView = findViewById(R.id.boton_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_clothes);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_clothes){
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(getApplicationContext(), Perfil.class));
                } else if (itemId == R.id.nav_add) {
                    startActivity(new Intent(getApplicationContext(), AddClothesAlbum.class));
                } else if (itemId == R.id.nav_home) {
                    startActivity(new Intent(getApplicationContext(), Home.class));
                }
                overridePendingTransition(0, 0);
                return true;
            }
        });

        findViewById(R.id.botonAll).setOnClickListener(v -> openCategory("all"));
        findViewById(R.id.botonPants).setOnClickListener(v -> openCategory("pants"));
        findViewById(R.id.botonDresses).setOnClickListener(v -> openCategory("dresses"));
        findViewById(R.id.botonShoes).setOnClickListener(v -> openCategory("shoes"));
        findViewById(R.id.botonAccessories).setOnClickListener(v -> openCategory("accesories"));
        findViewById(R.id.botonShirts).setOnClickListener(v -> openCategory("shirts"));

    }

    private void openCategory(String category) {
        Intent intent = new Intent(this, Clothes.class);
        intent.putExtra("CATEGORY", category);
        startActivity(intent);
    }
}