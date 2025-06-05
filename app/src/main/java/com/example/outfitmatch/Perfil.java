package com.example.outfitmatch;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.annotation.RequiresApi;

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

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class Perfil extends AppCompatActivity {
    private ConstraintLayout mainLayout;

    private FirebaseAuth mAuth;
    private TextView nombreUsuario, emailUsuario;
    private Button logoutButton, tema, idioma, delete;
    private ImageView profileImage, btnChangeMode;
    private ImageButton btnChangeLang, logoutImg, deleteImg;
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private boolean isSpanish, isDarkMode;
    private SmoothBottomBar bottomBar;

    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.applyTheme(this);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.setStatusBarColor(Color.TRANSPARENT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        setContentView(R.layout.activity_perfil);
        ConstraintLayout mainLayout = findViewById(R.id.main);

        boolean isDarkMode = (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
                || (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;

        if (isDarkMode) {
            mainLayout.setBackgroundResource(R.drawable.fondohomedarkperfil);
        } else {
            mainLayout.setBackgroundResource(R.drawable.fondohomeligthperfil);
        }


        nombreUsuario = findViewById(R.id.nameTextView);
        emailUsuario = findViewById(R.id.emailTextView);
        logoutButton = findViewById(R.id.logoutButton);
        profileImage = findViewById(R.id.profileImage);
        btnChangeLang = findViewById(R.id.btnChangeLang);
        tema = findViewById(R.id.temabotn);
        idioma = findViewById(R.id.lenguajeboton);
        delete = findViewById(R.id.deleteButton);

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

        logoutImg = findViewById(R.id.logoutimgview);
        deleteImg = findViewById(R.id.deleteimgview);

// Verificar el modo de tema actual
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            logoutImg.setImageResource(R.drawable.logoutdark);
            deleteImg.setImageResource(R.drawable.deletenight);
        } else {
            logoutImg.setImageResource(R.drawable.logoutlight);
            deleteImg.setImageResource(R.drawable.deletelight);
        }


        logoutButton.setOnClickListener(view -> mostrarDialogoCerrarSesion());
        profileImage.setOnClickListener(v -> openImagePicker());

        bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setItemActiveIndex(3); // Establecemos la posición en la que estamos (Perfil)

        bottomBar.setOnItemSelectedListener((OnItemSelectedListener) i -> {
            if (i == 3) return true; // Ya estamos en la página de Perfil

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


        Button deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(view -> mostrarDialogoEliminarCuenta());

        // Leer el idioma guardado
        String savedLang = getSavedLanguage();
        isSpanish = savedLang.equals("es");
        updateFlag();

        idioma.setOnClickListener(v -> {
            String newLang = isSpanish ? "en" : "es";
            if (!newLang.equals(getSavedLanguage())) {
                LanguageManager.setLocale(Perfil.this, newLang);
                saveLanguage(newLang);
                isSpanish = !isSpanish;
                overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_in);
                recreate();
            }
        });

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

        btnChangeMode.setOnClickListener(v -> {
            boolean isCurrentlyDarkMode = SettingsManager.isDarkModeEnabled(this);
            // Guardar la nueva preferencia
            SettingsManager.saveThemePreference(this, !isCurrentlyDarkMode);

            // Recrear la actividad actual para aplicar el nuevo tema
            recreate();
        });

        tema.setOnClickListener(v -> {
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
                            Bitmap bitmap;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), imageUri));
                            } else {
                                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                            }
                            profileImage.setImageBitmap(bitmap);
                            uploadImageToFirebase(imageUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
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
            progressDialog.setMessage(getString(R.string.actualizando_fotoperfil));
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

    private void mostrarDialogoCerrarSesion() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.cerrar_sesion))
                .setMessage(getString(R.string.seguro_cerrar_sesion))
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> cerrarSesion())
                .setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.dismiss())
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

    private void mostrarDialogoEliminarCuenta() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.eliminar_cuenta))
                .setMessage(getString(R.string.seguro_eliminar_cuenta))
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> eliminarCuenta())
                .setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void eliminarCuenta() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(Perfil.this, getString(R.string.cuenta_eliminada), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Perfil.this, StartActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Perfil.this, getString(R.string.error_eliminar_cuenta), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
