package com.example.outfitmatch;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Login extends AppCompatActivity {

    private EditText emailUsuario;
    private EditText passwordUsuario;
    private Button signInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        emailUsuario  = findViewById(R.id.etEmail);
        passwordUsuario = findViewById(R.id.etPassword);
        signInBtn = findViewById(R.id.btnSignIn); //aqui cambiamos la imagen de la flecha la convertimos en boton creo q quedaria mejor

        //luego llamamos a gestorUsuario para hacer la validacion
    }
}