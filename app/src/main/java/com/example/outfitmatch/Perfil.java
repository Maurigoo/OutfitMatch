package com.example.outfitmatch;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

public class Perfil extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView nombreUsuario, emailUsuario;
    private Button logoutButton;
    private ImageView profileImage;
    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        nombreUsuario = findViewById(R.id.nameTextView);
        emailUsuario = findViewById(R.id.emailTextView);
        logoutButton = findViewById(R.id.logoutButton);
        profileImage = findViewById(R.id.profileImage);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser usuarioActual = mAuth.getCurrentUser();

        if (usuarioActual != null) {
            String name = usuarioActual.getDisplayName();
            String email = usuarioActual.getEmail();

            nombreUsuario.setText(name != null ? name : "Nombre no disponible");
            emailUsuario.setText(email != null ? email : "Email no disponible");

            // Cargar la imagen de perfil solo si no está en caché
            loadProfileImage();
        } else {
            nombreUsuario.setText("No hay usuario autenticado");
            emailUsuario.setText("");
        }

        logoutButton.setOnClickListener(view -> mostrarDialogoCerrarSesion());

        // Configurar clic en la imagen de perfil para actualizarla
        profileImage.setOnClickListener(v -> openImagePicker());

        BottomNavigationView bottomNavigationView = findViewById(R.id.boton_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_profile) return true;
            if (itemId == R.id.nav_clothes) startActivity(new Intent(getApplicationContext(), Clothes.class));
            if (itemId == R.id.nav_add) startActivity(new Intent(getApplicationContext(), AddClothesAlbum.class));
            if (itemId == R.id.nav_home) startActivity(new Intent(getApplicationContext(), Home.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            return true;
        });
    }

    // Cargar la imagen de perfil desde Firebase si es necesario
    private void loadProfileImage() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            StorageReference storageRef = storage.getReference()
                    .child("profile_images/" + user.getUid() + ".jpg");

            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                // Usar Glide para cargar la imagen si la URL es válida
                Glide.with(this)
                        .load(uri)
                        .transform(new CircleCrop())  // Redondear la imagen
                        .into(profileImage);
            }).addOnFailureListener(e -> {
                // Si no se encuentra la imagen, cargar la imagen por defecto
                Glide.with(this)
                        .load(R.drawable.fotoperfil)
                        .transform(new CircleCrop())
                        .into(profileImage);
            });
        }
    }

    // Lanza el selector de imágenes para permitir al usuario elegir una nueva foto de perfil
    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    if (imageUri != null) {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                            profileImage.setImageBitmap(bitmap);
                            uploadImageToFirebase(imageUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    // Abre el selector de imágenes
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        imagePickerLauncher.launch(Intent.createChooser(intent, "Selecciona una imagen"));
    }

    // Subir la nueva imagen a Firebase
    private void uploadImageToFirebase(Uri imageUri) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            StorageReference storageRef = storage.getReference().child("profile_images/" + user.getUid() + ".jpg");

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Actualizando foto de perfil...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            storageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        loadProfileImage();
                        progressDialog.dismiss();
                    }))
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        e.printStackTrace();
                    });
        }
    }

    // Mostrar un cuadro de diálogo para confirmar el cierre de sesión
    private void mostrarDialogoCerrarSesion() {
        new AlertDialog.Builder(this)
                .setTitle("Cerrar sesión")
                .setMessage("¿Está seguro de que desea cerrar sesión?")
                .setPositiveButton("Sí", (dialog, which) -> cerrarSesion())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    // Cerrar sesión y redirigir a la pantalla de inicio
    private void cerrarSesion() {
        mAuth.signOut();
        Intent intent = new Intent(Perfil.this, StartActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
