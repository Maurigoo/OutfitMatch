package com.example.outfitmatch;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;

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
import com.bumptech.glide.Glide;

/**
 * Clase que permite a los usuarios agregar prendas desde la galería o una tienda.
 * También maneja la navegación entre diferentes secciones de la aplicación.
 */
public class AddClothesAlbum extends AppCompatActivity {

    private ImageButton buscarTienda, album;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private DaoPrenda daoPrenda;

    /**
     * Método llamado cuando se crea la actividad. Se encarga de inicializar la interfaz y los eventos de los botones.
     * @param savedInstanceState Estado previo de la actividad, si existe.
     */
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

    /**
     * Método que sube una imagen seleccionada a Firebase Storage.
     * @param imagenUri URI de la imagen seleccionada.
     */
    private void subirImagenAFirebase(Uri imagenUri) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Subiendo imagen...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            new Handler().postDelayed(() -> {
                String userId = user.getUid();
                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                StorageReference fileReference = storageRef.child("images/" + userId + "/" + System.currentTimeMillis() + ".jpg");

                // Subir la imagen a Firebase Storage
                fileReference.putFile(imagenUri)
                        .addOnSuccessListener(taskSnapshot -> {
                            fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                                String imageUrl = uri.toString();
                                mostrarDialogoExito();
                                cargarImagenEnUI(imageUrl);
                                progressDialog.dismiss();
                            });
                        })
                        .addOnFailureListener(e -> {
                            progressDialog.dismiss();
                        });
            }, 2000);
        }
    }

    /**
     * Método que muestra un diálogo de éxito cuando la imagen ha sido subida correctamente.
     */
    private void mostrarDialogoExito() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Éxito")
                .setMessage("Imagen subida con éxito")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    /**
     * Método que carga la imagen subida en la interfaz de usuario.
     * @param imageUrl URL de la imagen subida.
     */
    private void cargarImagenEnUI(String imageUrl) {
        ImageView imageView = findViewById(R.id.imageViewPrenda); // ImageView donde se mostrará la imagen
        Glide.with(this)
                .load(imageUrl) // Carga la imagen desde la URL
                .into(imageView); // Muestra la imagen en el ImageView
    }
}
