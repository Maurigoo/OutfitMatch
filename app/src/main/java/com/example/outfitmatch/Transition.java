package com.example.outfitmatch;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import com.example.outfitmatch.adaptador.AdaptadorTransition;
import com.example.outfitmatch.modelo.entidad.Prenda;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;
import java.util.List;

public class Transition extends AppCompatActivity {

    Button like, x, outfit;
    CardStackView cardStackView;
    CardStackLayoutManager manager;
    AdaptadorTransition adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transition);

        like = findViewById(R.id.botonLike);
        x = findViewById(R.id.botonX);
        outfit = findViewById(R.id.botonOufits);
        cardStackView = findViewById(R.id.cardStackView);

        setupCardStackView();

        outfit.setOnClickListener(view -> {
            Intent intent = new Intent(Transition.this, Outfits.class);
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
        manager = new CardStackLayoutManager(this);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(Direction.HORIZONTAL);
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);

        adapter = new AdaptadorTransition(getPrendas());
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);
    }

    private List<Prenda> getPrendas() {
        List<Prenda> prendas = new ArrayList<>();

        prendas.add(new Prenda("M", null, "Rojo", R.drawable.prenda1));
        prendas.add(new Prenda("L", null, "Azul", R.drawable.prenda2));
        prendas.add(new Prenda("S", null, "Negro", R.drawable.prenda3));

        return prendas;
    }
}