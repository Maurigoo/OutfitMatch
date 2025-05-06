package com.example.outfitmatch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.outfitmatch.modelo.entidad.Usuario;
import com.example.outfitmatch.modelo.negocio.GestorUsuario;

/**
 * Login es la actividad encargada de autenticar al usuario.
 * Permite a los usuarios iniciar sesión con su correo electrónico y contraseña,
 * redirigiéndolos a la pantalla principal en caso de éxito o mostrando mensajes de error.
 */
public class Login extends AppCompatActivity {

    private EditText emailUsuario;
    private EditText passwordUsuario;
    private Button signInbtnRound;
    private Button registerButton;
    private ProgressDialog progressDialog;
    private ImageButton ojo;
    private TextView forgotPasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.setStatusBarColor(Color.TRANSPARENT);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar vistas
        emailUsuario = findViewById(R.id.SignInEmail);
        passwordUsuario = findViewById(R.id.SignInPassword);
        signInbtnRound = findViewById(R.id.SignInBotonRound);
        registerButton = findViewById(R.id.registerButton);
        ojo = findViewById(R.id.ojo_login);
        forgotPasswordText = findViewById(R.id.forgotPasswordText);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Iniciando sesión...");
        progressDialog.setCancelable(false);

        signInbtnRound.setOnClickListener(view -> loginUser());

        registerButton.setOnClickListener(view -> {
            Intent intent = new Intent(Login.this, SignUp.class);
            startActivity(intent);
        });

        final boolean[] isPasswordVisible = {false};

        ojo.setOnClickListener(v -> {
            if (isPasswordVisible[0]) {
                passwordUsuario.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            } else {
                passwordUsuario.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }
            passwordUsuario.setSelection(passwordUsuario.getText().length());
            isPasswordVisible[0] = !isPasswordVisible[0];
        });

        forgotPasswordText.setOnClickListener(v -> mostrarDialogoRecuperacion());
    }

    private void loginUser() {
        String email = emailUsuario.getText().toString().trim();
        String password = passwordUsuario.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(Login.this, "Por favor, ingresa tu email y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setPassword(password);

        GestorUsuario.getInstance().iniciarSesion(usuario)
                .addOnSuccessListener(unused -> {
                    progressDialog.dismiss();
                    Toast.makeText(Login.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, Home.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(Login.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void mostrarDialogoRecuperacion() {
        EditText resetMail = new EditText(this);
        resetMail.setHint("Ingresa tu correo electrónico");

        new AlertDialog.Builder(this)
                .setTitle("Recuperar contraseña")
                .setMessage("Introduce tu correo para recibir un enlace de restablecimiento")
                .setView(resetMail)
                .setPositiveButton("Enviar", (dialog, which) -> {
                    String mail = resetMail.getText().toString().trim();
                    if (!TextUtils.isEmpty(mail)) {
                        GestorUsuario.getInstance().enviarResetPassword(mail)
                                .addOnSuccessListener(unused -> Toast.makeText(Login.this, "Correo enviado. Revisa tu bandeja de entrada", Toast.LENGTH_LONG).show())
                                .addOnFailureListener(e -> Toast.makeText(Login.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show());
                    } else {
                        Toast.makeText(Login.this, "Por favor introduce tu correo", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
