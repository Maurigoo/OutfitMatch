package com.example.outfitmatch;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.google.firebase.auth.FirebaseAuth;

import com.example.outfitmatch.modelo.entidad.Usuario;


public class Login extends AppCompatActivity {

    private EditText emailUsuario;
    private EditText passwordUsuario;
    private Button signInBtn;
    private ImageButton signInbtnRound;

    private FirebaseAuth mAuth; // Firebase Authentication

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        emailUsuario = findViewById(R.id.SignInEmail);
        passwordUsuario = findViewById(R.id.SignInPassword);
        signInBtn = findViewById(R.id.SignInBoton);
        signInbtnRound = findViewById(R.id.SignInBotonRound);

        // Configurar el listener para el botón de inicio de sesión
        signInBtn.setOnClickListener(view -> loginUser());
        signInbtnRound.setOnClickListener(view -> loginUser()); // Si el botón redondo hace lo mismo
    }

    private void loginUser() {
        String email = emailUsuario.getText().toString().trim();
        String password = passwordUsuario.getText().toString().trim();

        // Validaciones básicas
        if (TextUtils.isEmpty(email)) {
            emailUsuario.setError("El correo electrónico es obligatorio");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordUsuario.setError("La contraseña es obligatoria");
            return;
        }

        if (password.length() < 6) {
            passwordUsuario.setError("La contraseña debe tener al menos 6 caracteres");
            return;
        }

        // Autenticar usuario con Firebase
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Inicio de sesión exitoso
                        Toast.makeText(Login.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                        // Redirigir al usuario a la actividad principal
                        Intent intent = new Intent(Login.this, Home.class);
                        startActivity(intent);
                        finish(); // Cierra la actividad actual para que el usuario no pueda volver atrás
                    } else {
                        // Si el inicio de sesión falla, mostrar un mensaje de error
                        Toast.makeText(Login.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}