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
import androidx.core.content.ContextCompat;

import com.example.outfitmatch.modelo.persistencia.DaoPrenda;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Actividad que permite agregar prendas al álbum del usuario.
 * Incluye opciones para seleccionar una prenda de una tienda o
 * cargar imágenes desde el dispositivo.
 */
public class AddClothesAlbum extends AppCompatActivity {

    private ImageButton buscarTienda, album;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private DaoPrenda daoPrenda;

    /**
     * Método que se ejecuta cuando se crea la actividad. Inicializa los elementos de la interfaz,
     * configura los botones y las opciones de navegación.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clothes_album);

        daoPrenda = DaoPrenda.getInstance(); // Obtener la instancia de DaoPrenda
        configurarSelectorImagen(); // Configurar el selector de imágenes
        configurarBottomNavigation(); // Configurar la navegación inferior
        inicializarBotones(); // Inicializar los botones de acción
    }

    /**
     * Configura el selector de imágenes para permitir que el usuario seleccione una imagen
     * de su dispositivo. El resultado se maneja con un `ActivityResultLauncher`.
     */
    private void configurarSelectorImagen() {
        // Registrar el lanzador de actividad para seleccionar imágenes
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            // Si la imagen es seleccionada, se pasa la URI de la imagen a la siguiente actividad
                            Intent intent = new Intent(AddClothesAlbum.this, AddClothesDetails.class);
                            intent.putExtra("IMAGE_URI", selectedImageUri);
                            startActivity(intent);
                        } else {
                            // Si no se seleccionó ninguna imagen, se muestra un mensaje
                            Toast.makeText(this, "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    /**
     * Configura la barra de navegación inferior para navegar entre las pantallas de la aplicación
     * según el elemento seleccionado.
     */
    private void configurarBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.boton_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_add);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Class<?> targetActivity = null;
            int itemId = item.getItemId();

            // Determinar la actividad que se debe abrir según la opción seleccionada
            if (itemId == R.id.nav_add) return true;
            else if (itemId == R.id.nav_clothes) targetActivity = Clothes.class;
            else if (itemId == R.id.nav_profile) targetActivity = Perfil.class;
            else if (itemId == R.id.nav_home) targetActivity = Home.class;

            // Si se selecciona una actividad válida, se inicia la actividad correspondiente
            if (targetActivity != null) {
                startActivity(new Intent(getApplicationContext(), targetActivity));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
            return true;
        });
    }

    /**
     * Inicializa los botones de la actividad para abrir las opciones de búsqueda de tienda
     * y seleccionar una imagen para el álbum.
     */
    private void inicializarBotones() {
        // Inicializar los botones
        buscarTienda = findViewById(R.id.botonBuscarTienda);
        album = findViewById(R.id.botonAlbum);

        // Configurar el botón de búsqueda de tienda
        buscarTienda.setOnClickListener(v -> {
            startActivity(new Intent(AddClothesAlbum.this, AddClothesStore.class));
        });

        // Configurar el botón de álbum para seleccionar una imagen
        album.setOnClickListener(v -> {
            // Intent para seleccionar una imagen del dispositivo
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            activityResultLauncher.launch(intent); // Lanzar la actividad para seleccionar imagen
        });
    }
}
