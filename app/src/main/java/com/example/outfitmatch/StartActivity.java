package com.example.outfitmatch;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {

    Button login, signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        login = findViewById(R.id.loginButton);
        signUp = findViewById(R.id.signupButton);

        // Verificar si el usuario ya está autenticado
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            // Si el usuario ya está autenticado, redirigirlo a la actividad principal (por ejemplo, HomeActivity)
            Intent intent = new Intent(StartActivity.this, Home.class);
            startActivity(intent);
            finish(); // Evitar que el usuario regrese a esta pantalla
        } else {
            // Si el usuario no está autenticado, permitir que inicie sesión o se registre
            login.setOnClickListener(view -> {
                Intent intent = new Intent(StartActivity.this, Login.class);
                startActivity(intent);
            });

            signUp.setOnClickListener(view -> {
                Intent intent = new Intent(StartActivity.this, SignUp.class);
                startActivity(intent);
            });
        }
    }
}
