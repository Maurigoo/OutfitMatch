package com.example.outfitmatch;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.outfitmatch.modelo.entidad.Prenda;
import com.example.outfitmatch.modelo.negocio.GestorPrenda;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class Clothes extends AppCompatActivity {

    private TextView totalPrendasText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes);


        totalPrendasText = findViewById(R.id.totalPrendasText);



        ImageButton botonShirts = findViewById(R.id.botonShirts);
        ImageButton botonPants = findViewById(R.id.botonPants);
        ImageButton botonShoes = findViewById(R.id.botonShoes);
        ImageButton botonDresses = findViewById(R.id.botonDresses);
        ImageButton botonAccessories = findViewById(R.id.botonAccessories);
        ImageButton botonAll = findViewById(R.id.botonAll);

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
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    return true;
                } else if (itemId == R.id.nav_clothes) {
                    return true;
                } else if (itemId == R.id.nav_add) {
                    startActivity(new Intent(getApplicationContext(), AddClothesAlbum.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(getApplicationContext(), Perfil.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * Abre la actividad de lista de prendas según la categoría seleccionada.
     *
     * @param category Categoría de prendas a mostrar.
     */
    private void openClothesListActivity(String category) {
        Intent intent = new Intent(Clothes.this, ClothesListActivity.class);
        intent.putExtra("CATEGORY", category);
        startActivity(intent);
    }
}