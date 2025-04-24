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

    private EditText name, email, password;
    private ImageButton signUpRoundButton;
    private FirebaseAuth mAuth;
    private ImageButton ojo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        name = findViewById(R.id.SignUpName);
        email = findViewById(R.id.SignUpEmail);
        password = findViewById(R.id.SignUpPassword);
        signUpRoundButton = findViewById(R.id.SignUpBotonRound);
        ojo = findViewById(R.id.ojo_sing_up);

        signUpRoundButton.setOnClickListener(view -> registerUser());

        final boolean[] isPasswordVisible = {false};

        ojo.setOnClickListener(v -> {
            if (isPasswordVisible[0]) {
                password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            } else {
                password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }
            password.setSelection(password.getText().length());
            isPasswordVisible[0] = !isPasswordVisible[0];
        });
    }

    private void registerUser() {
        String nuevoName = name.getText().toString().trim();
        String nuevoEmail = email.getText().toString().trim();
        String nuevoPass = password.getText().toString().trim();

        if (TextUtils.isEmpty(nuevoName) || TextUtils.isEmpty(nuevoEmail) || TextUtils.isEmpty(nuevoPass)) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (nuevoPass.length() < 8) {
            Toast.makeText(this, "La contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registrando usuario...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(nuevoEmail, nuevoPass)
                .addOnCompleteListener(this, task -> {
                    progressDialog.dismiss();

                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(nuevoName)
                                    .build();
                            user.updateProfile(profileUpdates);
                        }

                        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();

                        Usuario usuario = new Usuario(nuevoName, nuevoEmail, nuevoPass);
                        Intent intent = new Intent(SignUp.this, StartActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
