package com.example.outfitmatch;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Perfil extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView nombreUsuario, emailUsuario;
    private Button logoutButton;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        nombreUsuario = findViewById(R.id.nameTextView);
        emailUsuario = findViewById(R.id.emailTextView);
        logoutButton = findViewById(R.id.logoutButton); // Botón de cerrar sesión

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser usuarioActual = mAuth.getCurrentUser();

        if (usuarioActual != null) {
            String name = usuarioActual.getDisplayName();
            String email = usuarioActual.getEmail();

            nombreUsuario.setText(name != null ? name : "Nombre no disponible");
            emailUsuario.setText(email != null ? email : "Email no disponible");
        } else {
            nombreUsuario.setText("No hay usuario autenticado");
            emailUsuario.setText("");
        }

        // Configurar el botón de cerrar sesión con un diálogo de confirmación
        logoutButton.setOnClickListener(view -> mostrarDialogoCerrarSesion());

        // Configuración de la barra de navegación inferior
        BottomNavigationView bottomNavigationView = findViewById(R.id.boton_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
                overridePendingTransition(0, 0);
                return true;
            }
        });
    }

    // Método para mostrar un diálogo de confirmación antes de cerrar sesión
    private void mostrarDialogoCerrarSesion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cerrar sesión");
        builder.setMessage("¿Está seguro de que desea cerrar sesión?");

        // Botón positivo (Sí)
        builder.setPositiveButton("Sí", (dialog, which) -> {
            cerrarSesion();
        });

        // Botón negativo (No)
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        // Mostrar el diálogo
        builder.create().show();
    }

    // Método para cerrar sesión
    private void cerrarSesion() {
        mAuth.signOut();  // Cerrar sesión en Firebase
        Intent intent = new Intent(Perfil.this, StartActivity.class);  // Redirigir a la pantalla de inicio
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  // Limpiar la pila de actividades
        startActivity(intent);
        finish();  // Finalizar esta actividad
    }
}
