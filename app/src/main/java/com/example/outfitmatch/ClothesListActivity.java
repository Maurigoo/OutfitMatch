package com.example.outfitmatch;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.outfitmatch.adaptador.AdaptadorClothes;
import com.example.outfitmatch.modelo.entidad.Prenda;

import java.util.ArrayList;
import java.util.List;

public class ClothesListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdaptadorClothes clothesAdapter;
    private List<Prenda> clothesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes_list);

        recyclerView = findViewById(R.id.recyclerViewClothes);
        // Cambia a GridLayoutManager con 2 columnas
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Obtener la categoría seleccionada desde el Intent
        String category = getIntent().getStringExtra("CATEGORY");

        if (category != null) {
            loadClothes(category);
        } else {
            Toast.makeText(this, "No category selected", Toast.LENGTH_SHORT).show();
        }

        // Configurar el adaptador
        clothesAdapter = new AdaptadorClothes(clothesList, prenda ->
                Toast.makeText(this, "Seleccionaste: " + prenda.getTalla(), Toast.LENGTH_SHORT).show()
        );
        recyclerView.setAdapter(clothesAdapter);
    }

    private void loadClothes(String category) {
        clothesList = new ArrayList<>();

        // Aquí debes cargar las prendas correspondientes a la categoría seleccionada
        switch (category) {
            case "Shirts":
                clothesList.add(new Prenda(R.drawable.basic_shirt, "M", "Algodón", "Blanco", "Shirts"));
                clothesList.add(new Prenda(R.drawable.basic_shirt, "32", "Denim", "Azul", "Shirts"));
                clothesList.add(new Prenda(R.drawable.shirt2, "L", "Seda", "Rojo", "Shirts"));
                break;
            case "Pants":
                clothesList.add(new Prenda(R.drawable.pants1, "32", "Denim", "Azul", "Pants"));
                clothesList.add(new Prenda(R.drawable.pants2, "34", "Lino", "Negro", "Pants"));
                break;
            case "Shoes":
                clothesList.add(new Prenda(R.drawable.shoes1, "42", "Cuero", "Marrón", "Shoes"));
                clothesList.add(new Prenda(R.drawable.shoes2, "40", "Sintético", "Negro", "Shoes"));
                break;
            case "Dresses":
                clothesList.add(new Prenda(R.drawable.dress1, "S", "Seda", "Rojo", "Dresses"));
                clothesList.add(new Prenda(R.drawable.dress2, "M", "Algodón", "Blanco", "Dresses"));
                break;
            case "Accessories":
                clothesList.add(new Prenda(R.drawable.accessory1, "Único", "Metal", "Plateado", "Accessories"));
                clothesList.add(new Prenda(R.drawable.accessory2, "Único", "Cuero", "Negro", "Accessories"));
                break;
            case "All":
                clothesList.add(new Prenda(R.drawable.basic_shirt, "M", "Algodón", "Blanco", "Shirts"));
                clothesList.add(new Prenda(R.drawable.pants1, "32", "Denim", "Azul", "Pants"));
                clothesList.add(new Prenda(R.drawable.shoes1, "42", "Cuero", "Marrón", "Shoes"));
                clothesList.add(new Prenda(R.drawable.dress1, "S", "Seda", "Rojo", "Dresses"));
                clothesList.add(new Prenda(R.drawable.accessory1, "Único", "Metal", "Plateado", "Accessories"));
                break;
            default:
                Toast.makeText(this, "Unknown category", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}