package com.example.outfitmatch;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.outfitmatch.modelo.entidad.Usuario;

public class SignUp extends AppCompatActivity {

    EditText name, email, password, phone;
    Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        name = findViewById(R.id.SignUpName);
        email = findViewById(R.id.SignUpEmail);
        password = findViewById(R.id.SignUpPassword);
        phone = findViewById(R.id.SignUpPhone);

        signUp = findViewById(R.id.SignUpBoton);

        signUp.setOnClickListener(view -> {
            String nuevoName = name.getText().toString();
            String nuevoEmail = email.getText().toString();
            String nuevoPass = password.getText().toString();
            String nuevoPhone = phone.getText().toString();

            if (!nuevoName.isEmpty() && !nuevoEmail.isEmpty() && !nuevoPass.isEmpty() && !nuevoPhone.isEmpty()) {
                Usuario usuario = new Usuario(nuevoName, nuevoEmail, nuevoPass, nuevoPhone);

                Intent intent = new Intent(SignUp.this, Login.class);
                intent.putExtra("usuario", usuario);
                setResult(RESULT_OK, intent);
                startActivity(intent);
            }

        });

    }
}