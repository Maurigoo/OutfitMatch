package com.example.outfitmatch;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.outfitmatch.modelo.persistencia.DaoPrenda;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

/**
 * Actividad que permite agregar prendas al álbum del usuario.
 * Incluye opciones para seleccionar una prenda de una tienda o
 * cargar imágenes desde el dispositivo.
 */
public class AddClothesAlbum extends AppCompatActivity {

    private ImageButton buscarTienda, album;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private DaoPrenda daoPrenda;
    private SmoothBottomBar bottomBar;

    /**
     * Método que se ejecuta cuando se crea la actividad. Inicializa los elementos de la interfaz,
     * configura los botones y las opciones de navegación.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clothes_album);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.setStatusBarColor(Color.TRANSPARENT);
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
        bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setItemActiveIndex(2); // Establecemos la posición en la que estamos (Perfil)

        bottomBar.setOnItemSelectedListener((OnItemSelectedListener) i -> {
            if (i == 2) return true; // Ya estamos en la página de Perfil

            Class<?> destination = null;
            switch (i) {
                case 0:
                    destination = Home.class; // Ir a Home
                    break;
                case 1:
                    destination = Clothes.class; // Ir a Clothes
                    break;
                case 2:
                    destination = AddClothesAlbum.class; // Ir a AddClothesAlbum
                    break;
                case 3:
                    destination = Perfil.class; // Ir a AddClothesStore
                    break;
            }

            if (destination != null) {
                startActivity(new Intent(getApplicationContext(), destination));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
