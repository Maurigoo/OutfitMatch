package com.example.outfitmatch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.outfitmatch.R;
import com.example.outfitmatch.StartActivity;
import com.example.outfitmatch.modelo.entidad.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUp extends AppCompatActivity {

    EditText name, email, password, phone;
    Button signUp;
    ImageButton signUpRoundButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        // Inicialización de Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Referencias a las vistas
        name = findViewById(R.id.SignUpName);
        email = findViewById(R.id.SignUpEmail);
        password = findViewById(R.id.SignUpPassword);
        phone = findViewById(R.id.SignUpPhone);
        signUp = findViewById(R.id.SignUpBoton);
        signUpRoundButton = findViewById(R.id.SignUpBotonRound);

        // Establecer los listeners
        signUp.setOnClickListener(view -> registerUser());
        signUpRoundButton.setOnClickListener(view -> registerUser());  // Usamos el mismo método para ambos botones
    }

    private void registerUser() {
        String nuevoName = name.getText().toString().trim();
        String nuevoEmail = email.getText().toString().trim();
        String nuevoPass = password.getText().toString().trim();
        String nuevoPhone = phone.getText().toString().trim();

        // Validaciones básicas
        if (TextUtils.isEmpty(nuevoName) || TextUtils.isEmpty(nuevoEmail) || TextUtils.isEmpty(nuevoPass) || TextUtils.isEmpty(nuevoPhone)) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (nuevoPass.length() < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear un ProgressDialog
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registrando usuario...");
        progressDialog.setCancelable(false); // El usuario no puede cancelar la operación
        progressDialog.show();

        // Registrar usuario en Firebase Authentication
        mAuth.createUserWithEmailAndPassword(nuevoEmail, nuevoPass)
                .addOnCompleteListener(this, task -> {
                    progressDialog.dismiss();  // Desaparecer el ProgressDialog una vez que se termine

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

                        // Redirigir al LoginActivity
                        Usuario usuario = new Usuario(nuevoName, nuevoEmail, nuevoPass, nuevoPhone);
                        Intent intent = new Intent(SignUp.this, StartActivity.class); // Redirige al StartActivity después del registro
                        startActivity(intent);
                        finish(); // Finaliza esta actividad para que el usuario no pueda regresar
                    } else {
                        Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
