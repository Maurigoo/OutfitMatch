package com.example.outfitmatch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

/**
 * AddClothesStore es una actividad que permite a los usuarios explorar tiendas de ropa en línea
 * y navegar de regreso al álbum de prendas de la aplicación.
 */
public class AddClothesStore extends AppCompatActivity {

    private ImageButton buscarAlbum; // Botón para regresar al álbum de prendas

    /**
     * Método llamado al crear la actividad. Inicializa la interfaz y configura los listeners.
     *
     * @param savedInstanceState Estado previamente guardado de la actividad (si aplica).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clothes_store);

        // Inicialización del botón para regresar al álbum
        buscarAlbum = findViewById(R.id.botonBuscarAlbum2);

        // Configura el clic para volver a AddClothesAlbum
        buscarAlbum.setOnClickListener(v -> {
            Intent intent = new Intent(AddClothesStore.this, AddClothesAlbum.class);
            startActivity(intent);
        });
    }

    /**
     * Abre el sitio web de Zara en el navegador predeterminado.
     *
     * @param view Vista que disparó el evento (usualmente un botón).
     */
    public void openZara(View view) {
        String url = "https://www.zara.com";
        openWebsite(url);
    }

    /**
     * Abre el sitio web de Bershka en el navegador predeterminado.
     *
     * @param view Vista que disparó el evento (usualmente un botón).
     */
    public void openBershka(View view) {
        String url = "https://www.bershka.com";
        openWebsite(url);
    }

    /**
     * Método auxiliar para abrir una URL en el navegador predeterminado.
     *
     * @param url La URL del sitio web que se desea abrir.
     */
    private void openWebsite(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}
