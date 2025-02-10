package com.example.outfitmatch;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Clothes extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_clothes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.botonAll).setOnClickListener(v -> openCategory("all"));
        findViewById(R.id.botonPants).setOnClickListener(v -> openCategory("pants"));
        findViewById(R.id.botonDresses).setOnClickListener(v -> openCategory("dresses"));
        findViewById(R.id.botonShoes).setOnClickListener(v -> openCategory("shoes"));
        findViewById(R.id.botonAccessories).setOnClickListener(v -> openCategory("accesories"));
        findViewById(R.id.botonShirts).setOnClickListener(v -> openCategory("shirts"));

    }

    private void openCategory(String category) {
        Intent intent = new Intent(this, Clothes.class);
        intent.putExtra("CATEGORY", category);
        startActivity(intent);
    }
}