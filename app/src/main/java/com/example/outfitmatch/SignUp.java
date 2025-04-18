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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.outfitmatch.modelo.entidad.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * SignUp es la actividad que permite a los usuarios crear una nueva cuenta.
 * Utiliza Firebase Authentication para registrar nuevos usuarios.
 */
public class SignUp extends AppCompatActivity {

    private EditText name, email, password, phone;     // Campos de entrada para el registro
    private Button signUp;                             // Botón de registro principal
    private ImageButton signUpRoundButton;             // Botón redondo de registro
    private FirebaseAuth mAuth;                        // Instancia de FirebaseAuth
    private ImageButton ojo;

    /**
     * Método llamado al crear la actividad. Inicializa vistas y configuración.
     *
     * @param savedInstanceState Estado previamente guardado de la actividad (si aplica).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        // Inicialización de Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Referencias a las vistas
        name = findViewById(R.id.SignUpName);
        email = findViewById(R.id.SignUpEmail);
        password = findViewById(R.id.SignUpPassword);
        signUp = findViewById(R.id.SignUpBoton);
        signUpRoundButton = findViewById(R.id.SignUpBotonRound);
        ojo = findViewById(R.id.ojo_sing_up);

        // Establecer listeners para ambos botones de registro
        signUp.setOnClickListener(view -> registerUser());
        signUpRoundButton.setOnClickListener(view -> registerUser());  // Usa el mismo método

        final boolean[] isPasswordVisible = {false};

        ojo.setOnClickListener(v -> {
            if (isPasswordVisible[0]) {
                // Ocultar contraseña
                password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            } else {
                // Mostrar contraseña
                password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }

            // Mantener el cursor al final del texto
            password.setSelection(password.getText().length());
            isPasswordVisible[0] = !isPasswordVisible[0];
        });
    }

    /**
     * Método encargado de registrar un nuevo usuario utilizando Firebase Authentication.
     * Incluye validaciones de campos y muestra mensajes en caso de error.
     */
    private void registerUser() {
        String nuevoName = name.getText().toString().trim();
        String nuevoEmail = email.getText().toString().trim();
        String nuevoPass = password.getText().toString().trim();
        String nuevoPhone = phone.getText().toString().trim();

        // Validaciones básicas de campos
        if (TextUtils.isEmpty(nuevoName) || TextUtils.isEmpty(nuevoEmail) || TextUtils.isEmpty(nuevoPass) || TextUtils.isEmpty(nuevoPhone)) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (nuevoPass.length() < 8) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mostrar ProgressDialog durante el proceso de registro
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registrando usuario...");
        progressDialog.setCancelable(false); // No permitir cancelación
        progressDialog.show();

        // Registrar usuario en Firebase Authentication
        mAuth.createUserWithEmailAndPassword(nuevoEmail, nuevoPass)
                .addOnCompleteListener(this, task -> {
                    progressDialog.dismiss();  // Ocultar el ProgressDialog al finalizar

                    if (task.isSuccessful()) {
                        // Obtener el usuario registrado
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Guardar el nombre del usuario en Firebase
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(nuevoName)
                                    .build();
                            user.updateProfile(profileUpdates);
                        }

                        // Mostrar mensaje de éxito
                        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();

                        // Redirigir al StartActivity
                        Usuario usuario = new Usuario(nuevoName, nuevoEmail, nuevoPass, nuevoPhone);
                        Intent intent = new Intent(SignUp.this, StartActivity.class);
                        startActivity(intent);
                        finish(); // Finaliza la actividad para que no se pueda volver atrás
                    } else {
                        // Mostrar mensaje de error si el registro falla
                        Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
