package com.example.outfitmatch;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * AddClothesStore es una actividad que permite a los usuarios explorar tiendas de ropa en línea
 * y navegar de regreso al álbum de prendas de la aplicación.
 */
public class AddClothesStore extends AppCompatActivity {

    private ImageButton buscarAlbum;

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
        buscarAlbum = findViewById(R.id.botonBuscarAlbum2);

        buscarAlbum.setOnClickListener(v -> {
            Intent intent = new Intent(AddClothesStore.this, AddClothesAlbum.class);
            startActivity(intent);
        });
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
        BottomNavigationView bottomNavigationView = findViewById(R.id.boton_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_add);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Class<?> targetActivity = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_add) return true;
            else if (itemId == R.id.nav_clothes) targetActivity = Clothes.class;
            else if (itemId == R.id.nav_profile) targetActivity = Perfil.class;
            else if (itemId == R.id.nav_home) targetActivity = Home.class;

            if (targetActivity != null) {
                startActivity(new Intent(getApplicationContext(), targetActivity));
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();
            }
            return true;
        });
    }
}
