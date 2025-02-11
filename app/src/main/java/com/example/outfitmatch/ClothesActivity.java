package com.example.outfitmatch;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ClothesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes);

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

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home){
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
    }

    // Método para abrir la actividad de lista de ropa
    private void openClothesListActivity(String category) {
        Intent intent = new Intent(ClothesActivity.this, ClothesListActivity.class);
        intent.putExtra("CATEGORY", category);  // Pasar la categoría seleccionada
        startActivity(intent);
    }
}