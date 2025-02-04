package com.example.outfitmatch;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Slide;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Home extends AppCompatActivity {

    Button articles, ideas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        articles = findViewById(R.id.botonArticles);
        ideas = findViewById(R.id.botonIdeas);

        articles.setOnClickListener(view -> {
            Intent intent = new Intent(Home.this, Clothes.class);
            startActivity(intent);
        });

        ideas.setOnClickListener(view -> {
            Intent intent = new Intent(Home.this, Transition.class);
            startActivity(intent);
        });
    }
}
