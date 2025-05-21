package com.example.outfitmatch;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import me.ibrahimsn.lib.SmoothBottomBar;

public class GenerarOutfit extends AppCompatActivity {

    private SmoothBottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.applyTheme(this);
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.setStatusBarColor(Color.TRANSPARENT);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_generar_outfit);

        // Ajustar insets para la vista principal
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configurar la barra de navegación inferior
        bottomBar = findViewById(R.id.bottomBar);

        bottomBar.setOnItemSelectedListener(new Function1<Integer, Unit>() {
            @Override
            public Unit invoke(Integer index) {
                if (index == 4) return Unit.INSTANCE; // Ya estás en esta pestaña

                Class<?> destination = null;
                switch (index) {
                    case 0:
                        destination = Home.class;
                        break;
                    case 1:
                        destination = Clothes.class;
                        break;
                    case 2:
                        destination = AddClothesAlbum.class;
                        break;
                    case 3:
                        destination = Perfil.class;
                        break;
                    case 4:
                        destination = GenerarOutfit.class;
                        break;
                }

                if (destination != null) {
                    startActivity(new Intent(getApplicationContext(), destination));
                    overridePendingTransition(0, 0);
                }

                return Unit.INSTANCE; // Kotlin's void
            }
        });

    }
}
