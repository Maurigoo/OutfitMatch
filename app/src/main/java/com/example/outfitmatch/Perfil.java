package com.example.outfitmatch;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.outfitmatch.util.LanguageManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.Locale;

public class Perfil extends AppCompatActivity {
    private ConstraintLayout mainLayout;

    private FirebaseAuth mAuth;
    private TextView nombreUsuario, emailUsuario;
    private Button logoutButton;
    private ImageView profileImage;
    private ImageButton btnChangeLang;
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private boolean isSpanish;
    private ImageView btnChangeMode;
    private boolean isDarkMode;


    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.applyTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        nombreUsuario = findViewById(R.id.nameTextView);
        emailUsuario = findViewById(R.id.emailTextView);
        logoutButton = findViewById(R.id.logoutButton);
        profileImage = findViewById(R.id.profileImage);
        btnChangeLang = findViewById(R.id.btnChangeLang);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser usuarioActual = mAuth.getCurrentUser();

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

        logoutButton.setOnClickListener(view -> mostrarDialogoCerrarSesion());
        profileImage.setOnClickListener(v -> openImagePicker());

        BottomNavigationView bottomNavigationView = findViewById(R.id.boton_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_profile) {
                return true;
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

        Button deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(view -> mostrarDialogoEliminarCuenta());


        // Leer el idioma guardado
        String savedLang = getSavedLanguage();
        isSpanish = savedLang.equals("es");
        updateFlag();

        btnChangeLang.setOnClickListener(v -> {
            String newLang = isSpanish ? "en" : "es";
            if (!newLang.equals(getSavedLanguage())) {
                LanguageManager.setLocale(Perfil.this, newLang);
                saveLanguage(newLang);
                isSpanish = !isSpanish;
                overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_in);
                recreate();
            }
        });
        mainLayout = findViewById(R.id.main);


        btnChangeMode = findViewById(R.id.btnChangeMode);
        isDarkMode = (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES);
        updateIcon(isDarkMode);

        /**
        btnChangeMode.setOnClickListener(v -> {
            int currentMode = AppCompatDelegate.getDefaultNightMode();
            if (currentMode == AppCompatDelegate.MODE_NIGHT_YES) {
                // Cambiar a modo claro
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                mainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.primaryColor));
            } else {
                // Cambiar a modo oscuro
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                mainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            }
            recreate();
        });

    **/

        btnChangeMode.setOnClickListener(v -> {
            boolean isCurrentlyDarkMode = SettingsManager.isDarkModeEnabled(this);
            // Guardar la nueva preferencia
            SettingsManager.saveThemePreference(this, !isCurrentlyDarkMode);

            // Recrear la actividad actual para aplicar el nuevo tema
            recreate();
        });


    }

    private void saveThemePreference(boolean darkModeEnabled) {
        // Guardar preferencia en SharedPreferences
        getSharedPreferences("settings", MODE_PRIVATE)
                .edit()
                .putBoolean("is_dark_mode", darkModeEnabled)
                .apply();
    }

    private void updateIcon(boolean darkMode) {
        // Cambiar el icono entre la luna y el sol
        if (darkMode) {
            btnChangeMode.setImageResource(R.drawable.moonicon);  // Icono de luna para modo claro

        } else {
            btnChangeMode.setImageResource(R.drawable.suniconwhite);  // Icono de sol para modo oscuro
        }
    }

    private void updateFlag() {
        if (isSpanish) {
            btnChangeLang.setImageResource(R.drawable.flag_spain);
        } else {
            btnChangeLang.setImageResource(R.drawable.flag_uk);
        }
    }

    private String getSavedLanguage() {
        return getSharedPreferences("settings", MODE_PRIVATE)
                .getString("app_language", Locale.getDefault().getLanguage());
    }

    private void saveLanguage(String lang) {
        getSharedPreferences("settings", MODE_PRIVATE)
                .edit()
                .putString("app_language", lang)
                .apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String savedLang = getSavedLanguage();
        isSpanish = savedLang.equals("es");
        updateFlag();
    }

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

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        imagePickerLauncher.launch(Intent.createChooser(intent, "Selecciona una imagen"));
    }

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

    private void loadProfileImage() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            StorageReference storageRef = storage.getReference()
                    .child("profile_images/" + user.getUid() + ".jpg");

            storageRef.getDownloadUrl().addOnSuccessListener(uri -> Glide.with(this)
                            .load(uri)
                            .transform(new CircleCrop())
                            .into(profileImage))
                    .addOnFailureListener(e -> {
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

    private void cerrarSesion() {
        mAuth.signOut();
        Intent intent = new Intent(Perfil.this, StartActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Muestra un cuadro de diálogo para confirmar la eliminación de la cuenta.
     */
    private void mostrarDialogoEliminarCuenta() {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar cuenta")
                .setMessage("¿Estás seguro de que deseas eliminar tu cuenta? Esta acción no se puede deshacer.")
                .setPositiveButton("Eliminar", (dialog, which) -> eliminarCuenta())
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    /**
     * Elimina la cuenta del usuario autenticado.
     */
    private void eliminarCuenta() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Eliminando cuenta...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            user.delete()
                    .addOnCompleteListener(task -> {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(Perfil.this, StartActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            new AlertDialog.Builder(this)
                                    .setTitle("Error")
                                    .setMessage("No se pudo eliminar la cuenta. Por favor, vuelve a iniciar sesión y vuelve a intentarlo.")
                                    .setPositiveButton("OK", null)
                                    .create()
                                    .show();
                        }
                    });
        }
    }

}
