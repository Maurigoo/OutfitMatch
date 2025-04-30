package com.example.outfitmatch;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.outfitmatch.util.LanguageManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {

    private Button login, signUp;
    private ImageButton languageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.applyTheme(this);
        LanguageManager.loadLocale(this); // Cargar idioma antes del layout
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(false);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        login = findViewById(R.id.loginButton);
        signUp = findViewById(R.id.signupButton);
        languageButton = findViewById(R.id.languageButton);

        // Cargar idioma actual y asignar bandera
        String lang = PreferenceManager.getDefaultSharedPreferences(this)
                .getString("app_lang", "es");

        if (lang.equals("es")) {
            languageButton.setImageResource(R.drawable.flag_spain);
        } else {
            languageButton.setImageResource(R.drawable.flag_uk);
        }

        // Listener para cambiar idioma
        languageButton.setOnClickListener(v -> {
            String currentLang = PreferenceManager.getDefaultSharedPreferences(this)
                    .getString("app_lang", "es");
            String newLang = currentLang.equals("es") ? "en" : "es";

            LanguageManager.setLocale(this, newLang);

            if (newLang.equals("es")) {
                languageButton.setImageResource(R.drawable.flag_spain);
            } else {
                languageButton.setImageResource(R.drawable.flag_uk);
            }

            recreateActivityWithoutAnimation();
        });

        // Autenticación Firebase
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            startActivity(new Intent(StartActivity.this, Home.class));
            finish();
        } else {
            login.setOnClickListener(view -> startActivity(new Intent(StartActivity.this, Login.class)));
            signUp.setOnClickListener(view -> startActivity(new Intent(StartActivity.this, SignUp.class)));
        }
    }

    private void recreateActivityWithoutAnimation() {
        overridePendingTransition(0, 0);
        recreate();
        overridePendingTransition(0, 0);
    }
}
