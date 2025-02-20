package com.example.outfitmatch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.outfitmatch.modelo.entidad.Usuario;
import com.example.outfitmatch.modelo.negocio.GestorUsuario;

public class Login extends AppCompatActivity {

    private EditText emailUsuario;
    private EditText passwordUsuario;
    private Button signInBtn;
    private ImageButton signInbtnRound;
    private Button registerButton; // Botón para redirigir al registro
    private ProgressDialog progressDialog;  // Diálogo de progreso

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar vistas
        emailUsuario = findViewById(R.id.SignInEmail);
        passwordUsuario = findViewById(R.id.SignInPassword);
        signInBtn = findViewById(R.id.SignInBoton);
        signInbtnRound = findViewById(R.id.SignInBotonRound);
        registerButton = findViewById(R.id.registerButton); // Botón de registro

        // Configurar el ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Iniciando sesión...");  // Mensaje que aparecerá en el diálogo
        progressDialog.setCancelable(false);  // No permitir que el usuario cierre el diálogo

        // Configurar el clic del botón de inicio de sesión
        signInBtn.setOnClickListener(view -> loginUser());
        signInbtnRound.setOnClickListener(view -> loginUser());

        // Configurar el clic del botón de registro
        registerButton.setOnClickListener(view -> {
            // Redirigir a la actividad de registro
            Intent intent = new Intent(Login.this, SignUp.class);
            startActivity(intent);
        });
    }

    private void loginUser() {
        // Obtener los valores de los campos de texto
        String email = emailUsuario.getText().toString().trim();
        String password = passwordUsuario.getText().toString().trim();

        // Validar que los campos no estén vacíos
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(Login.this, "Por favor, ingresa tu email y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mostrar el ProgressDialog
        progressDialog.show();

        // Crear un objeto Usuario con los datos ingresados
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setPassword(password);

        // Intentar iniciar sesión
        GestorUsuario.getInstance().iniciarSesion(usuario)
                .addOnSuccessListener(unused -> {
                    // Ocultar el ProgressDialog
                    progressDialog.dismiss();

                    // Mostrar mensaje de éxito y redirigir a la pantalla principal
                    Toast.makeText(Login.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, Home.class);
                    startActivity(intent);
                    finish();  // Finalizar la actividad de login
                })
                .addOnFailureListener(e -> {
                    // Ocultar el ProgressDialog en caso de error
                    progressDialog.dismiss();

                    // Mostrar mensaje de error
                    Toast.makeText(Login.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}