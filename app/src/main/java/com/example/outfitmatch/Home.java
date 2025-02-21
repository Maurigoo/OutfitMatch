package com.example.outfitmatch;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;
import java.util.Random;

public class Home extends AppCompatActivity {

    Button articles, ideas, outfit;
    ImageView gifImageView; // ImageView para el GIF
    TextView tvWeather; // TextView para mostrar clima

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Inicialización de vistas
        gifImageView = findViewById(R.id.gifImageView);
        tvWeather = findViewById(R.id.tvWeather); // TextView para clima

        // Cargar el GIF usando Glide
        Glide.with(this)
                .load(R.drawable.hanger_animation) // Asegúrate de que el GIF esté en res/drawable
                .into(gifImageView);

        // Configurar BottomNavigationView
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

        // Configurar botones
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

        // Inicializar cliente de ubicación
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtener ubicación y clima
        getWeatherData();
    }

    // Obtener datos de ubicación y clima
    private void getWeatherData() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        fetchWeather(location.getLatitude(), location.getLongitude());
                    } else {
                        Toast.makeText(this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Generar datos simulados de clima
    private void fetchWeather(double lat, double lon) {
        // Generar datos de clima simulados
        Random random = new Random();
        int tempActual = random.nextInt(15) + 10; // Genera una temperatura entre 10°C y 25°C
        int tempMin = tempActual - random.nextInt(3); // Min 2-3 grados menos
        int tempMax = tempActual + random.nextInt(3); // Max 2-3 grados más

        String[] weatherConditions = {"Soleado", "Nublado", "Lluvia ligera", "Tormenta", "Niebla"};
        String weatherCondition = weatherConditions[random.nextInt(weatherConditions.length)];

        // Mostrar en el TextView
        runOnUiThread(() -> {
            String simulatedWeather = String.format(Locale.getDefault(),
                    "Condición: %s\nTemp actual: %d°C\nMin: %d°C / Max: %d°C",
                    weatherCondition, tempActual, tempMin, tempMax);
            tvWeather.setText(simulatedWeather);
        });
    }

    // Manejar respuesta de permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getWeatherData();
        } else {
            Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
        }
    }
}
