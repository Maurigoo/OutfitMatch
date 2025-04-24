package com.example.outfitmatch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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

    private EditText emailUsuario;       // Campo para ingresar el email
    private EditText passwordUsuario;    // Campo para ingresar la contraseña
    private ImageButton signInbtnRound;  // Botón redondo alternativo de inicio de sesión
    private Button registerButton;       // Botón para redirigir al registro
    private ProgressDialog progressDialog;  // Diálogo de progreso para mostrar mientras se autentica
    private ImageButton ojo;

    /**
     * Método llamado al crear la actividad. Inicializa vistas y configura listeners.
     *
     * @param savedInstanceState Estado previamente guardado de la actividad (si aplica).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar vistas
        emailUsuario = findViewById(R.id.SignInEmail);
        passwordUsuario = findViewById(R.id.SignInPassword);
        signInbtnRound = findViewById(R.id.SignInBotonRound);
        registerButton = findViewById(R.id.registerButton); // Botón de registro
        ojo = findViewById(R.id.ojo_login);


        // Configurar el ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Iniciando sesión...");  // Mensaje mostrado durante la autenticación
        progressDialog.setCancelable(false);               // No permitir que el usuario cierre el diálogo

        // Configurar el clic del botón de inicio de sesión
        signInbtnRound.setOnClickListener(view -> loginUser());

        // Configurar el clic del botón de registro
        registerButton.setOnClickListener(view -> {
            // Redirigir a la actividad de registro
            Intent intent = new Intent(Login.this, SignUp.class);
            startActivity(intent);
        });

        final boolean[] isPasswordVisible = {false};

        ojo.setOnClickListener(v -> {
            if (isPasswordVisible[0]) {
                // Ocultar contraseña
                passwordUsuario.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            } else {
                // Mostrar contraseña
                passwordUsuario.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }

            // Mantener el cursor al final del texto
            passwordUsuario.setSelection(passwordUsuario.getText().length());
            isPasswordVisible[0] = !isPasswordVisible[0];
        });
    }
        /**
         * Método encargado de autenticar al usuario utilizando el correo y la contraseña ingresados.
         * Muestra mensajes de error si los campos están vacíos o si la autenticación falla.
         */
        private void loginUser() {
            // Obtener los valores de los campos de texto
            String email = emailUsuario.getText().toString().trim();
            String password = passwordUsuario.getText().toString().trim();

            // Validar que los campos no estén vacíos
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(Login.this, "Por favor, ingresa tu email y contraseña", Toast.LENGTH_SHORT).show();
                return;
            }

            // Mostrar el ProgressDialog mientras se realiza la autenticación
            progressDialog.show();

            // Crear un objeto Usuario con los datos ingresados
            Usuario usuario = new Usuario();
            usuario.setEmail(email);
            usuario.setPassword(password);

            // Intentar iniciar sesión utilizando GestorUsuario
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
