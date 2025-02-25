package com.example.outfitmatch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.outfitmatch.adaptador.AdaptadorTransition;
import com.example.outfitmatch.modelo.entidad.Prenda;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;
import java.util.List;

public class Transition extends AppCompatActivity {

    Button like, x, outfit;
    CardStackView cardStackView;
    CardStackLayoutManager manager;
    AdaptadorTransition adapter;

    private List<Prenda> savedOutfits = new ArrayList<>();
    private List<Prenda> prendas = new ArrayList<>(); // Lista de prendas desde Firestore

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);

        BottomNavigationView bottomNavigationView = findViewById(R.id.boton_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                startActivity(new Intent(getApplicationContext(), Home.class));
            } else if (itemId == R.id.nav_clothes) {
                startActivity(new Intent(getApplicationContext(), Clothes.class));
            } else if (itemId == R.id.nav_add) {
                startActivity(new Intent(getApplicationContext(), AddClothesAlbum.class));
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(getApplicationContext(), Perfil.class));
            }

            overridePendingTransition(0, 0);
            return true;
        });

        like = findViewById(R.id.botonLike);
        x = findViewById(R.id.botonX);
        outfit = findViewById(R.id.botonOufits);
        cardStackView = findViewById(R.id.cardStackView);

        setupCardStackView();
        loadPrendasFromFirestore(); // Carga prendas desde Firestore

        outfit.setOnClickListener(view -> {
            Intent intent = new Intent(Transition.this, Outfits.class);
            intent.putExtra("savedOutfits", (ArrayList<Prenda>) savedOutfits);
            startActivity(intent);
        });

        like.setOnClickListener(view -> swipeCard(Direction.Right));
        x.setOnClickListener(view -> swipeCard(Direction.Left));
    }

    private void swipeCard(Direction direction) {
        SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                .setDirection(direction).setDuration(200).build();
        manager.setSwipeAnimationSetting(setting);
        cardStackView.swipe();
    }

    private void setupCardStackView() {
        manager = new CardStackLayoutManager(this, new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) { }

            @Override
            public void onCardSwiped(Direction direction) {
                if (direction == Direction.Right) {
                    int topPosition = manager.getTopPosition() - 1;
                    if (topPosition >= 0 && topPosition < prendas.size()) {
                        Prenda prenda = prendas.get(topPosition);
                        savedOutfits.add(prenda);
                        Toast.makeText(Transition.this, "Añadido a favoritos", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCardRewound() { }

            @Override
            public void onCardCanceled() { }

            @Override
            public void onCardAppeared(View view, int position) { }

            @Override
            public void onCardDisappeared(View view, int position) { }
        });

        manager.setStackFrom(StackFrom.None);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(Direction.HORIZONTAL);
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);

        adapter = new AdaptadorTransition(prendas);
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);
    }

    private void loadPrendasFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("prendas").document(userId).collection("user_prendas")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        prendas.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String imagenUrl = document.getString("imagenUrl");
                            String talla = document.getString("talla");
                            String material = document.getString("material");
                            String color = document.getString("color");
                            String tipo = document.getString("tipo");

                            Prenda prenda = new Prenda(0, talla, material, color, tipo);
                            prenda.setImagenUrl(imagenUrl); // Establecer URL de imagen

                            prendas.add(prenda);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Error al cargar prendas", Toast.LENGTH_SHORT).show();
                        Log.e("Firestore", "Error al obtener prendas", task.getException());
                    }
                });
    }
}
