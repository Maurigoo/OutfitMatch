package com.example.outfitmatch;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * StartActivity es la actividad de inicio que verifica si el usuario ya está autenticado.
 * Si el usuario está autenticado, lo redirige a la actividad principal (Home).
 * Si no está autenticado, le permite iniciar sesión o registrarse.
 */
public class StartActivity extends AppCompatActivity {

    private Button login;   // Botón para iniciar sesión
    private Button signUp;  // Botón para registrarse

    /**
     * Método llamado al crear la actividad. Verifica el estado de autenticación y configura las vistas.
     *
     * @param savedInstanceState Estado previamente guardado de la actividad (si aplica).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);  // Habilita diseño Edge-to-Edge
        setContentView(R.layout.activity_start);

        // Configurar compatibilidad con vectores en AppCompat
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(false);

        // Ajustar el diseño para tener en cuenta los Insets del sistema (barras de estado, navegación, etc.)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Referencias a los botones
        login = findViewById(R.id.loginButton);
        signUp = findViewById(R.id.signupButton);

        // Verificar si el usuario ya está autenticado
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            // Si el usuario ya está autenticado, redirigirlo a la actividad principal (Home)
            Intent intent = new Intent(StartActivity.this, Home.class);
            startActivity(intent);
            finish();  // Finaliza esta actividad para que el usuario no pueda regresar con el botón "Atrás"
        } else {
            // Si el usuario no está autenticado, configurar los botones para Login y SignUp

            // Redirige al LoginActivity
            login.setOnClickListener(view -> {
                Intent intent = new Intent(StartActivity.this, Login.class);
                startActivity(intent);
            });

            // Redirige al SignUpActivity
            signUp.setOnClickListener(view -> {
                Intent intent = new Intent(StartActivity.this, SignUp.class);
                startActivity(intent);
            });
        }
    }
}
