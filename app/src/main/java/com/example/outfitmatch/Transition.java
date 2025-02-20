package com.example.outfitmatch;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.outfitmatch.Outfits;
import com.example.outfitmatch.R;
import com.example.outfitmatch.adaptador.AdaptadorTransition;
import com.example.outfitmatch.modelo.entidad.Prenda;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeableMethod;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.StackFrom;

import java.util.ArrayList;
import java.util.List;

public class Transition extends AppCompatActivity {

    Button like, x, outfit;
    CardStackView cardStackView;
    CardStackLayoutManager manager;
    AdaptadorTransition adapter;

    private List<Prenda> savedOutfits = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);

        BottomNavigationView bottomNavigationView = findViewById(R.id.boton_navigation);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
            }
        });

        like = findViewById(R.id.botonLike);
        x = findViewById(R.id.botonX);
        outfit = findViewById(R.id.botonOufits);
        cardStackView = findViewById(R.id.cardStackView);

        setupCardStackView();

        outfit.setOnClickListener(view -> {
            Intent intent = new Intent(Transition.this, Outfits.class);
            intent.putExtra("savedOutfits", (ArrayList<Prenda>) savedOutfits);
            startActivity(intent);
        });

        like.setOnClickListener(view -> {
            int topPosition = manager.getTopPosition();
            if (topPosition > 0) {
                Prenda prenda = getPrendas().get(topPosition - 1);
                savedOutfits.add(prenda);
                saveOutfitsToPreferences(savedOutfits);
                swipeCard(Direction.Right);
            }
        });

        x.setOnClickListener(view -> swipeCard(Direction.Left));
    }

    private void swipeCard(Direction direction) {
        SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                .setDirection(direction).setDuration(200).build();
        manager.setSwipeAnimationSetting(setting);
        cardStackView.swipe();
    }

    private void setupCardStackView() {
        // Initialize the CardStackLayoutManager with CardStackListener
        manager = new CardStackLayoutManager(this, new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {
                // Handle card dragging
            }

            @Override
            public void onCardSwiped(Direction direction) {
                if (direction == Direction.Right) {
                    int topPosition = manager.getTopPosition() - 1;
                    if (topPosition >= 0) {
                        Prenda prenda = getPrendas().get(topPosition);
                        savedOutfits.add(prenda);
                        saveOutfitsToPreferences(savedOutfits);
                    }
                }
            }

            @Override
            public void onCardRewound() {
                // Handle card rewind
            }

            @Override
            public void onCardCanceled() {
                // Handle card cancel
            }

            @Override
            public void onCardAppeared(View view, int position) {
                // Handle card appearance
            }

            @Override
            public void onCardDisappeared(View view, int position) {
                // Handle card disappearance
            }
        });

        // Set properties for the manager
        manager.setStackFrom(StackFrom.None); // Corrected usage
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(Direction.HORIZONTAL);
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);

        // Set up the adapter for the view
        adapter = new AdaptadorTransition(getPrendas());
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);
    }

    private List<Prenda> getPrendas() {
        List<Prenda> prendas = new ArrayList<>();
        prendas.add(new Prenda(R.drawable.prenda1, "M", "Rojo", "Algodón"));
        prendas.add(new Prenda(R.drawable.prenda2, "L", "Azul", "Poliéster"));
        prendas.add(new Prenda(R.drawable.prenda3, "S", "Negro", "Seda"));
        return prendas;
    }

    private void saveOutfitsToPreferences(List<Prenda> outfits) {
        // Save the list of outfits in SharedPreferences
    }
}
