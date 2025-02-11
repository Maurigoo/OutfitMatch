package com.example.outfitmatch;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.outfitmatch.adaptador.AdaptadorClothes;
import com.example.outfitmatch.modelo.entidad.Prenda;

import java.util.ArrayList;
import java.util.List;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Clothes extends AppCompatActivity {

    //private RecyclerView recyclerView;
    private AdaptadorClothes adapter;

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


//        recyclerView = findViewById(R.id.recyclerViewClothes);
    //    recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Obtener la categoría pasada desde ClothesActivity
        String categoria = getIntent().getStringExtra("categoria");

        // Generar la lista de imágenes según la categoría
        List<Integer> imagenes = obtenerImagenesPorCategoria(categoria);

        // Configurar el adaptador con las imágenes obtenidas
        List<Prenda> prendas = new ArrayList<>();
        for (Integer imagen : imagenes) {
            prendas.add(new Prenda(imagen, "M", "Algodón", "Blanco"));
        }


        adapter = new AdaptadorClothes(prendas, prenda ->
                Toast.makeText(this, "Seleccionaste: " + prenda.getTalla(), Toast.LENGTH_SHORT).show()
        );
        //recyclerView.setAdapter(adapter);
    }

    private List<Integer> obtenerImagenesPorCategoria(String categoria) {
        List<Integer> imagenes = new ArrayList<>();
        switch (categoria) {
            case "shirts":
                imagenes.add(R.drawable.shirt1);
                imagenes.add(R.drawable.shirt2);
                imagenes.add(R.drawable.shirt3);
                break;
            case "pants":
                imagenes.add(R.drawable.pants1);
                imagenes.add(R.drawable.pants2);
                imagenes.add(R.drawable.pants3);
                break;
            case "shoes":
                imagenes.add(R.drawable.shoes1);
                imagenes.add(R.drawable.shoes2);
                imagenes.add(R.drawable.shoes3);
                break;
            case "dresses":
                imagenes.add(R.drawable.dress1);
                imagenes.add(R.drawable.dress2);
                imagenes.add(R.drawable.dress3);
                break;
            case "accessories":
                imagenes.add(R.drawable.accessory1);
                imagenes.add(R.drawable.accessory2);
                imagenes.add(R.drawable.accessory3);
                break;
            case "all":
                imagenes.add(R.drawable.shirt1);
                imagenes.add(R.drawable.pants1);
                imagenes.add(R.drawable.shoes1);
                imagenes.add(R.drawable.dress1);
                imagenes.add(R.drawable.accessory1);
                break;
        }
        return imagenes;
    }
}