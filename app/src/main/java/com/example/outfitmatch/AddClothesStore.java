package com.example.outfitmatch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddClothesStore extends AppCompatActivity {

    ImageButton buscarAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clothes_store);

        buscarAlbum = findViewById(R.id.botonBuscarAlbum2);

        buscarAlbum.setOnClickListener(v -> {
            Intent intent = new Intent(AddClothesStore.this, AddClothesAlbum.class);
            startActivity(intent);
        });

    }

    public void openZara(View view) {
        String url = "https://www.zara.com";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    public void openBershka(View view) {
        String url = "https://www.bershka.com";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}