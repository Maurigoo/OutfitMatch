package com.example.outfitmatch;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.outfitmatch.modelo.negocio.GestorUsuario;
import com.example.outfitmatch.modelo.entidad.Usuario;

public class Login extends AppCompatActivity {

    private EditText emailUsuario;
    private EditText passwordUsuario;
    private Button signInBtn;
    private ImageButton signInbtnRound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailUsuario = findViewById(R.id.SignInEmail);
        passwordUsuario = findViewById(R.id.SignInPassword);
        signInBtn = findViewById(R.id.SignInBoton);
        signInbtnRound = findViewById(R.id.SignInBotonRound);

        signInBtn.setOnClickListener(view -> loginUser());
        signInbtnRound.setOnClickListener(view -> loginUser());
    }

    private void loginUser() {
        String email = emailUsuario.getText().toString().trim();
        String password = passwordUsuario.getText().toString().trim();

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setPassword(password);

        GestorUsuario.getInstance().iniciarSesion(usuario)
                .addOnSuccessListener(unused -> {

                    Toast.makeText(Login.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, Home.class);

                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {

                    Toast.makeText(Login.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
