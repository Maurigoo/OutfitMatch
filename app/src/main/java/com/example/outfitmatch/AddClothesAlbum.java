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

public class AddClothesAlbum extends AppCompatActivity {

    private ImageButton buscarTienda, album;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private DaoPrenda daoPrenda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clothes_album);

        daoPrenda = DaoPrenda.getInstance();
        configurarSelectorImagen();
        configurarBottomNavigation();
        inicializarBotones();
    }

    private void configurarSelectorImagen() {
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            Intent intent = new Intent(AddClothesAlbum.this, AddClothesDetails.class);
                            intent.putExtra("IMAGE_URI", selectedImageUri.toString());
                            startActivity(intent);
                        } else {
                            Toast.makeText(this, "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

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
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
            return true;
        });
    }

    private void inicializarBotones() {
        buscarTienda = findViewById(R.id.botonBuscarTienda);
        album = findViewById(R.id.botonAlbum);

        buscarTienda.setOnClickListener(v -> {
            startActivity(new Intent(AddClothesAlbum.this, AddClothesStore.class));
        });

        album.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            activityResultLauncher.launch(intent);
        });
    }
}
