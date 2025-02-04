package com.example.outfitmatch;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Transition extends AppCompatActivity {

    ImageButton like, x, outfit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transition);


        like = findViewById(R.id.botonLike);
        x = findViewById(R.id.botonX);
        outfit = findViewById(R.id.botonOutfit);

        outfit.setOnClickListener(view -> {
            Intent intent = new Intent(Transition.this, Outfits.class);
            startActivity(intent);
        });


    }
}