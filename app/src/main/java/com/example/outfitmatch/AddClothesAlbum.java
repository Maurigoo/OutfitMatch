package com.example.outfitmatch;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.outfitmatch.modelo.persistencia.DaoPrenda;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.bumptech.glide.Glide;


public class AddClothesAlbum extends AppCompatActivity {

    private ImageButton buscarTienda, album;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private DaoPrenda daoPrenda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clothes_album);

        daoPrenda = DaoPrenda.getInstance();

        // Inicializa el ActivityResultLauncher para seleccionar imágenes
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        subirImagenAFirebase(selectedImageUri);
                    }
                }
        );

        // Configuración de la barra de navegación
        BottomNavigationView bottomNavigationView = findViewById(R.id.boton_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_add);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_add) {
                    return true;
                } else if (itemId == R.id.nav_clothes) {
                    startActivity(new Intent(getApplicationContext(), Clothes.class));
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(getApplicationContext(), Perfil.class));
                } else if (itemId == R.id.nav_home) {
                    startActivity(new Intent(getApplicationContext(), Home.class));
                }
                overridePendingTransition(0, 0);
                return true;
            }
        });

        // Inicializa los botones
        buscarTienda = findViewById(R.id.botonBuscarTienda);
        album = findViewById(R.id.botonAlbum);

        buscarTienda.setOnClickListener(v -> {
            Intent intent = new Intent(AddClothesAlbum.this, AddClothesStore.class);
            startActivity(intent);
        });

        album.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            activityResultLauncher.launch(intent);
        });
    }

    private void subirImagenAFirebase(Uri imagenUri) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference fileReference = storageRef.child("images/" + userId + "/" + System.currentTimeMillis() + ".jpg");

            // Subir la imagen a Firebase Storage
            fileReference.putFile(imagenUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Obtener la URL de la imagen subida
                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            // Ahora tienes la URL de descarga
                            String imageUrl = uri.toString();
                            // Aquí puedes usar la URL de la imagen para actualizar la UI o guardarla en tu base de datos
                            Toast.makeText(this, "Imagen subida con éxito", Toast.LENGTH_SHORT).show();
                            cargarImagenEnUI(imageUrl); // Llamada a la función que carga la imagen en la interfaz
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error al subir imagen: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "Debes iniciar sesión para subir imágenes", Toast.LENGTH_SHORT).show();
        }
    }

    private void cargarImagenEnUI(String imageUrl) {
        ImageView imageView = findViewById(R.id.imageViewPrenda); // Aquí pones el ID de tu ImageView
        Glide.with(this)
                .load(imageUrl) // La URL de la imagen subida
                .into(imageView); // Carga la imagen en el ImageView
    }

}
