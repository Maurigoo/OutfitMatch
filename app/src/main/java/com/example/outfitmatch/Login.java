package com.example.outfitmatch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.outfitmatch.R;
import com.example.outfitmatch.modelo.entidad.Usuario;
import com.example.outfitmatch.modelo.negocio.GestorUsuario;

public class Login extends AppCompatActivity {

    private EditText emailUsuario;
    private EditText passwordUsuario;
    private Button signInBtn;
    private ImageButton signInbtnRound;
    private ProgressDialog progressDialog;  // Declarar el ProgressDialog

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailUsuario = findViewById(R.id.SignInEmail);
        passwordUsuario = findViewById(R.id.SignInPassword);
        signInBtn = findViewById(R.id.SignInBoton);
        signInbtnRound = findViewById(R.id.SignInBotonRound);

        // Inicializar el ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Iniciando sesión...");  // Mensaje que aparecerá en el diálogo
        progressDialog.setCancelable(false);  // No permitir que el usuario cierre el diálogo

        signInBtn.setOnClickListener(view -> loginUser());
        signInbtnRound.setOnClickListener(view -> loginUser());
    }

    private void loginUser() {
        String email = emailUsuario.getText().toString().trim();
        String password = passwordUsuario.getText().toString().trim();

        // Validar los campos
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(Login.this, "Por favor, ingresa tu email y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mostrar el ProgressDialog antes de intentar iniciar sesión
        progressDialog.show();

        // Crear el usuario
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setPassword(password);

        // Intentar iniciar sesión
        GestorUsuario.getInstance().iniciarSesion(usuario)
                .addOnSuccessListener(unused -> {
                    // Ocultar el ProgressDialog una vez que el inicio de sesión haya sido exitoso
                    progressDialog.dismiss();

                    Toast.makeText(Login.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, Home.class);
                    startActivity(intent);
                    finish();  // Finaliza la actividad de login
                })
                .addOnFailureListener(e -> {
                    // Ocultar el ProgressDialog si ocurre un error
                    progressDialog.dismiss();

                    Toast.makeText(Login.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
