package com.example.outfitmatch;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

/**
 * AddClothesStore es una actividad que permite a los usuarios explorar tiendas de ropa en línea
 * y navegar de regreso al álbum de prendas de la aplicación.
 */
public class AddClothesStore extends AppCompatActivity {

    private ImageButton buscarAlbum, buscarTienda, botonZara, botonBershka, botonPull, botonLefties;
    private SmoothBottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.setStatusBarColor(Color.TRANSPARENT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clothes_store);
        configurarBottomNavigation();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish(); // o startActivity si quieres volver explícitamente
                overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
            }
        });


        buscarAlbum = findViewById(R.id.botonBuscarAlbum2);
        buscarTienda = findViewById(R.id.botonBuscarTienda2);
        botonZara = findViewById(R.id.botonZara);
        botonBershka = findViewById(R.id.botonBershka);
        botonPull = findViewById(R.id.botonPull);
        botonLefties = findViewById(R.id.botonLefties);

        buscarAlbum.setOnClickListener(v -> {
            Intent intent = new Intent(AddClothesStore.this, AddClothesAlbum.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        });

        int nightModeFlags = getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK;

        if (nightModeFlags == android.content.res.Configuration.UI_MODE_NIGHT_YES) {
            // Modo oscuro
            buscarTienda.setImageResource(R.drawable.busquedark); // Ícono oscuro para fondo oscuro
            buscarAlbum.setImageResource(R.drawable.galeriadark);

            botonZara.setImageResource(R.drawable.img_zara_dark);
            botonBershka.setImageResource(R.drawable.img_bershka_dark);
            botonPull.setImageResource(R.drawable.img_pull_dark);
            botonLefties.setImageResource(R.drawable.img_lefties_dark);
        } else {
            // Modo claro
            buscarTienda.setImageResource(R.drawable.busquedalight); // Ícono claro para fondo claro
            buscarAlbum.setImageResource(R.drawable.galerialight);

            botonZara.setImageResource(R.drawable.img_zara);
            botonBershka.setImageResource(R.drawable.img_bershka);
            botonPull.setImageResource(R.drawable.img_pull);
            botonLefties.setImageResource(R.drawable.img_lefties);
        }
    }

    /**
     * Método para abrir el sitio web de la tienda correspondiente.
     */
    public void openStore(View view) {
        String url = "";

        if (view.getId() == R.id.botonZara) {
            url = "https://www.zara.com";
        } else if (view.getId() == R.id.botonBershka) {
            url = "https://www.bershka.com";
        } else if (view.getId() == R.id.botonPull) {
            url = "https://www.pullandbear.com";
        } else if (view.getId() == R.id.botonLefties) {
            url = "https://www.lefties.com";
        }

        openWebsite(url);
    }



    /**
     * Método auxiliar para abrir una URL en el navegador predeterminado.
     */
    private void openWebsite(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    /**
     * Configura la barra de navegación inferior y su comportamiento al seleccionar opciones.
     */
    private void configurarBottomNavigation() {
        // Configurar la barra de navegación inferior
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
                    destination = AddClothesStore.class; // Ir a AddClothesAlbum
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
}
