package com.example.outfitmatch;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.outfitmatch.modelo.persistencia.DaoPrenda;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * AddClothesAlbum es una actividad que permite al usuario seleccionar imágenes
 * desde su galería para agregar nuevas prendas o buscar prendas en la tienda.
 * También incluye una barra de navegación inferior para moverse entre pantallas.
 */
public class AddClothesAlbum extends AppCompatActivity {

    private ImageButton buscarTienda, album;  // Botones para buscar en tienda y abrir álbum
    private ActivityResultLauncher<Intent> activityResultLauncher;  // Para seleccionar imágenes
    private DaoPrenda daoPrenda;  // DAO para gestionar operaciones relacionadas con prendas

    /**
     * Método llamado al crear la actividad. Inicializa la UI y configura los listeners.
     *
     * @param savedInstanceState Estado previamente guardado de la actividad (si aplica).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clothes_album);

        daoPrenda = DaoPrenda.getInstance();

        // Inicializa el ActivityResultLauncher para seleccionar imágenes desde la galería
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();

                        if (selectedImageUri != null) {
                            // Lanzar AddClothesDetailsActivity con la URI seleccionada
                            Intent intent = new Intent(AddClothesAlbum.this, AddClothesDetails.class);
                            intent.putExtra("IMAGE_URI", selectedImageUri);
                            startActivity(intent);
                        } else {
                            Toast.makeText(this, "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        // Configuración de la barra de navegación inferior
        BottomNavigationView bottomNavigationView = findViewById(R.id.boton_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_add);  // Selecciona el ítem actual

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_add) {
                    return true;  // Ya estamos en esta pantalla
                } else if (itemId == R.id.nav_clothes) {
                    startActivity(new Intent(getApplicationContext(), Clothes.class));
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(getApplicationContext(), Perfil.class));
                } else if (itemId == R.id.nav_home) {
                    startActivity(new Intent(getApplicationContext(), Home.class));
                }
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            }
        });

        // Inicializa los botones
        buscarTienda = findViewById(R.id.botonBuscarTienda);
        album = findViewById(R.id.botonAlbum);

        // Configura la acción para buscar prendas en la tienda
        buscarTienda.setOnClickListener(v -> {
            Intent intent = new Intent(AddClothesAlbum.this, AddClothesStore.class);
            startActivity(intent);
        });

        // Configura la acción para seleccionar una imagen de la galería
        album.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            activityResultLauncher.launch(intent);
        });
    }
}
