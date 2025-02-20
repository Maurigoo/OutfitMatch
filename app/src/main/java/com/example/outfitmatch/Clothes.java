package com.example.outfitmatch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.outfitmatch.modelo.negocio.GestorPrenda;
import com.example.outfitmatch.modelo.entidad.Prenda;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class Clothes extends AppCompatActivity {

    private int shirts = 0;
    private int pants = 0;
    private int shoes = 0;
    private int dresses = 0;
    private int accessories = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes);

        // Obtener el total de prendas
        GestorPrenda.getInstance().obtenerTotalPrendas("userId", new GestorPrenda.OnTotalPrendasListener() {
            @Override
            public void onTotalPrendas(int total, List<Prenda> prendas) {
                // Contar las prendas por tipo
                for (Prenda prenda : prendas) {
                    String tipo = prenda.getTipo();
                    switch (tipo) {
                        case "Shirts":
                            shirts++;
                            break;
                        case "Pants":
                            pants++;
                            break;
                        case "Shoes":
                            shoes++;
                            break;
                        case "Dresses":
                            dresses++;
                            break;
                        case "Accessories":
                            accessories++;
                            break;
                    }
                }

                // Mostrar los resultados en Log (puedes hacerlo también en la UI)
                Log.d("Clothes", "Shirts: " + shirts);
                Log.d("Clothes", "Pants: " + pants);
                Log.d("Clothes", "Shoes: " + shoes);
                Log.d("Clothes", "Dresses: " + dresses);
                Log.d("Clothes", "Accessories: " + accessories);
            }
        });

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