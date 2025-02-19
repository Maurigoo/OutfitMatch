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
    private PieChart pieChart;
    private TextView totalPrendasText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes);

        pieChart = findViewById(R.id.pieChart);
        totalPrendasText = findViewById(R.id.totalPrendasText);

        cargarGrafico();


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
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.nav_clothes) {
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

    /**
     * Carga el gráfico circular con los datos de las prendas.
     */
    private void cargarGrafico() {
        String userId = "USER_ID_AQUI"; // Reemplaza con el ID del usuario actual

        GestorPrenda.getInstance().obtenerTotalPrendas(userId, (total, prendas) -> {
            totalPrendasText.setText(total + " prendas");

            List<PieEntry> entries = new ArrayList<>();

            int shirts = 0, pants = 0, shoes = 0, dresses = 0, accessories = 0;
            for (Prenda prenda : prendas) {
                String tipo = prenda.getMaterial(); // Ajusta si hay un campo específico para tipo
                switch (tipo) {
                    case "Algodón":
                        shirts++;
                        break;
                    case "Denim":
                        pants++;
                        break;
                    case "Cuero":
                        shoes++;
                        break;
                    case "Seda":
                        dresses++;
                        break;
                    case "Metal":
                        accessories++;
                        break;
                }
            }

            // Solo agregar entradas con valores > 0
            if (shirts > 0) entries.add(new PieEntry(shirts, "Shirts"));
            if (pants > 0) entries.add(new PieEntry(pants, "Pants"));
            if (shoes > 0) entries.add(new PieEntry(shoes, "Shoes"));
            if (dresses > 0) entries.add(new PieEntry(dresses, "Dresses"));
            if (accessories > 0) entries.add(new PieEntry(accessories, "Accessories"));

            // Configuración del DataSet con tonos azules
            List<Integer> blueShades = new ArrayList<>();
            blueShades.add(Color.parseColor("#4A90E2"));
            blueShades.add(Color.parseColor("#50E3C2"));
            blueShades.add(Color.parseColor("#007AFF"));
            blueShades.add(Color.parseColor("#5AC8FA"));
            blueShades.add(Color.parseColor("#34AADC"));

            PieDataSet dataSet = new PieDataSet(entries, "Prendas");
            dataSet.setColors(blueShades);
            dataSet.setValueTextSize(14f);
            dataSet.setValueTextColor(Color.WHITE);
            dataSet.setSliceSpace(2f); // Espacio entre las secciones

            PieData pieData = new PieData(dataSet);
            pieChart.setData(pieData);

            // Configuraciones adicionales para el gráfico completo
            pieChart.getDescription().setEnabled(false);
            pieChart.setUsePercentValues(true);
            pieChart.setDrawHoleEnabled(false); // Eliminar el hueco central
            pieChart.setEntryLabelColor(Color.BLACK);
            pieChart.setEntryLabelTextSize(12f);
            pieChart.getLegend().setEnabled(true);

            pieChart.invalidate(); // Refrescar el gráfico
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
