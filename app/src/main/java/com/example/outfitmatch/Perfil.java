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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

/**
 * Perfil es la actividad que permite a los usuarios ver y actualizar su información personal,
 * incluyendo nombre, correo electrónico y foto de perfil. También ofrece la opción de cerrar sesión.
 */
public class Perfil extends AppCompatActivity {

    private FirebaseAuth mAuth;                          // Instancia de FirebaseAuth para autenticación
    private TextView nombreUsuario, emailUsuario;        // Vistas para mostrar el nombre y correo del usuario
    private Button logoutButton;                         // Botón para cerrar sesión
    private ImageView profileImage;                      // Imagen de perfil del usuario
    private final FirebaseStorage storage = FirebaseStorage.getInstance(); // Instancia de FirebaseStorage

    /**
     * Método llamado al crear la actividad. Inicializa vistas y configura lógica de usuario.
     *
     * @param savedInstanceState Estado previamente guardado de la actividad (si aplica).
     */
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        // Inicialización de vistas
        nombreUsuario = findViewById(R.id.nameTextView);
        emailUsuario = findViewById(R.id.emailTextView);
        logoutButton = findViewById(R.id.logoutButton);
        profileImage = findViewById(R.id.profileImage);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser usuarioActual = mAuth.getCurrentUser();

        // Mostrar información del usuario autenticado
        if (usuarioActual != null) {
            String name = usuarioActual.getDisplayName();
            String email = usuarioActual.getEmail();

            nombreUsuario.setText(name != null ? name : "Nombre no disponible");
            emailUsuario.setText(email != null ? email : "Email no disponible");
            loadProfileImage();
        } else {
            nombreUsuario.setText("No hay usuario autenticado");
            emailUsuario.setText("");
        }

        // Configurar el botón de cerrar sesión
        logoutButton.setOnClickListener(view -> mostrarDialogoCerrarSesion());

        // Configurar clic en la imagen de perfil para actualizarla
        profileImage.setOnClickListener(v -> openImagePicker());

        // Configurar la barra de navegación inferior
        BottomNavigationView bottomNavigationView = findViewById(R.id.boton_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_profile) {
                return true;  // Ya estamos en el perfil
            } else if (itemId == R.id.nav_clothes) {
                startActivity(new Intent(getApplicationContext(), Clothes.class));
            } else if (itemId == R.id.nav_add) {
                startActivity(new Intent(getApplicationContext(), AddClothesAlbum.class));
            } else if (itemId == R.id.nav_home) {
                startActivity(new Intent(getApplicationContext(), Home.class));
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            return true;
        });
    }

    /**
     * Lanzador para seleccionar una imagen desde la galería para la foto de perfil.
     */
    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    if (imageUri != null) {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                            profileImage.setImageBitmap(bitmap);  // Mostrar la imagen seleccionada
                            uploadImageToFirebase(imageUri);      // Subir la imagen a Firebase
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    /**
     * Abre el selector de imágenes para permitir al usuario elegir una nueva foto de perfil.
     */
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        imagePickerLauncher.launch(Intent.createChooser(intent, "Selecciona una imagen"));
    }

    /**
     * Sube la imagen seleccionada a Firebase Storage y actualiza la foto de perfil.
     *
     * @param imageUri URI de la imagen seleccionada.
     */
    private void uploadImageToFirebase(Uri imageUri) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            StorageReference storageRef = storage.getReference().child("profile_images/" + user.getUid() + ".jpg");

            // Mostrar ProgressDialog mientras se sube la imagen
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Actualizando foto de perfil...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            storageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        loadProfileImage(); // Actualizar la imagen en el perfil
                        progressDialog.dismiss(); // Ocultar ProgressDialog al finalizar
                    }))
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss(); // Ocultar ProgressDialog en caso de error
                        e.printStackTrace();
                    });
        }
    }

    /**
     * Carga la foto de perfil desde Firebase Storage y la muestra en la vista.
     * Si no se encuentra la imagen, se carga una por defecto.
     */
    private void loadProfileImage() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            StorageReference storageRef = storage.getReference()
                    .child("profile_images/" + user.getUid() + ".jpg");

            storageRef.getDownloadUrl().addOnSuccessListener(uri -> Glide.with(this)
                            .load(uri)
                            .transform(new CircleCrop())  // Redondear la imagen
                            .into(profileImage))
                    .addOnFailureListener(e -> {
                        // En caso de fallo, mostrar una imagen por defecto
                        Glide.with(this)
                                .load(R.drawable.fotoperfil)
                                .transform(new CircleCrop())
                                .into(profileImage);
                    });
        }
    }

    /**
     * Muestra un cuadro de diálogo para confirmar el cierre de sesión.
     */
    private void mostrarDialogoCerrarSesion() {
        new AlertDialog.Builder(this)
                .setTitle("Cerrar sesión")
                .setMessage("¿Está seguro de que desea cerrar sesión?")
                .setPositiveButton("Sí", (dialog, which) -> cerrarSesion())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    /**
     * Cierra la sesión del usuario y redirige a la pantalla de inicio (StartActivity).
     */
    private void cerrarSesion() {
        mAuth.signOut();
        Intent intent = new Intent(Perfil.this, StartActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
