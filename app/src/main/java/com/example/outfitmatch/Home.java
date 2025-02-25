package com.example.outfitmatch;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.outfitmatch.API.ClimaAPI;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Home es la actividad principal de la aplicación.
 * Muestra información personalizada para el usuario, el clima actual basado en la ubicación
 * y permite la navegación entre diferentes secciones mediante una barra inferior.
 */
public class Home extends AppCompatActivity {

    private Button articles, ideas, outfit; // Botones para navegar a diferentes secciones
    private ImageView gifImageView;         // Imagen animada usando Glide
    private TextView tvWeather, userGreetingTextView; // Muestra clima y saludo al usuario

    private FusedLocationProviderClient fusedLocationClient; // Cliente para obtener la ubicación
    private static final String API_KEY = "a7bc60d2c1304f9cad2150757252402"; // Clave API de WeatherAPI
    private FirebaseAuth mAuth; // Manejo de autenticación con Firebase

    /**
     * Método llamado al crear la actividad. Inicializa vistas, clima y navegación.
     *
     * @param savedInstanceState Estado previamente guardado de la actividad (si aplica).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Inicialización de vistas
        gifImageView = findViewById(R.id.gifImageView);
        tvWeather = findViewById(R.id.tvWeather);
        userGreetingTextView = findViewById(R.id.saludoUsuario);
        mAuth = FirebaseAuth.getInstance();

        // Configurar saludo personalizado para el usuario actual
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String name = currentUser.getDisplayName();
            userGreetingTextView.setText(name != null ? "Hello, " + name : "Hello, User");
        } else {
            userGreetingTextView.setText("Hello, Guest");
        }

        // Cargar animación con Glide
        Glide.with(this).load(R.drawable.hanger_animation).into(gifImageView);

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
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
        getWeatherData();
    }

    /**
     * Obtiene la ubicación actual del usuario y llama a fetchWeather para obtener el clima.
     */
    private void getWeatherData() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Solicitar permisos si no están concedidos
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                fetchWeather(location.getLatitude(), location.getLongitude());
            } else {
                Toast.makeText(this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Llama a la API de clima usando Retrofit para obtener datos basados en la ubicación.
     *
     * @param lat Latitud actual del usuario.
     * @param lon Longitud actual del usuario.
     */
    private void fetchWeather(double lat, double lon) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.weatherapi.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ClimaAPI api = retrofit.create(ClimaAPI.class);
        Call<Clima> call = api.getClima(API_KEY, lat + "," + lon);

        call.enqueue(new Callback<Clima>() {
            @Override
            public void onResponse(Call<Clima> call, Response<Clima> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Clima clima = response.body();
                    String weatherInfo = String.format(Locale.getDefault(),
                            "\uD83D\uDCCD %s, %s\n⛅ %s\n\uD83C\uDF21 %d°C",
                            clima.getLocation().getName(),
                            clima.getLocation().getCountry(),
                            clima.getCurrent().getCondition().getText(),
                            (int) clima.getCurrent().getTemp_c());
                    tvWeather.setText(weatherInfo);
                }
            }

            @Override
            public void onFailure(Call<Clima> call, Throwable t) {
                Toast.makeText(Home.this, "Error al obtener el clima", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Maneja la respuesta del usuario al cuadro de diálogo de permisos.
     *
     * @param requestCode  Código de solicitud.
     * @param permissions  Lista de permisos solicitados.
     * @param grantResults Resultados de la solicitud de permisos.
     */
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
