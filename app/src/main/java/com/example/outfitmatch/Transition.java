package com.example.outfitmatch;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.outfitmatch.adaptador.AdaptadorTransition;
import com.example.outfitmatch.modelo.entidad.Prenda;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import me.ibrahimsn.lib.SmoothBottomBar;

public class Transition extends AppCompatActivity {

    private Button like, x, favorito;
    private CardStackView cardStackView;
    private CardStackLayoutManager manager;
    private AdaptadorTransition adapter;

    private List<Prenda> savedOutfits = new ArrayList<>();
    private List<Prenda> prendas = new ArrayList<>();

    private FirebaseFirestore db;
    private String userId;

    private SmoothBottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.setStatusBarColor(Color.TRANSPARENT);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);

        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Limpiar outfits guardados previamente (opcional, dependiendo del uso)
        clearSavedOutfitsInFirestore();

        bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setOnItemSelectedListener(new Function1<Integer, Unit>() {
            @Override
            public Unit invoke(Integer index) {
                if (index == 4) return Unit.INSTANCE;

                Class<?> destination = null;
                switch (index) {
                    case 0: destination = Home.class; break;
                    case 1: destination = Clothes.class; break;
                    case 2: destination = AddClothesAlbum.class; break;
                    case 3: destination = Perfil.class; break;
                    case 4: destination = GenerarOutfit.class; break;
                }

                if (destination != null) {
                    startActivity(new Intent(getApplicationContext(), destination));
                    overridePendingTransition(0, 0);
                }

                return Unit.INSTANCE;
            }
        });

        like = findViewById(R.id.botonLike);
        x = findViewById(R.id.botonX);
        favorito = findViewById(R.id.botonfavorito);
        cardStackView = findViewById(R.id.cardStackView);

        setupCardStackView();
        loadPrendasFromFirestore();

        favorito.setOnClickListener(view -> {
            Intent intent = new Intent(Transition.this, Favorito.class);
            startActivity(intent);
        });

        like.setOnClickListener(view -> swipeCard(Direction.Right));
        x.setOnClickListener(view -> swipeCard(Direction.Left));
    }

    private void swipeCard(Direction direction) {
        SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                .setDirection(direction)
                .setDuration(200)
                .build();
        manager.setSwipeAnimationSetting(setting);
        cardStackView.swipe();
    }

    private void setupCardStackView() {
        manager = new CardStackLayoutManager(this, new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) { }

            @Override
            public void onCardSwiped(Direction direction) {
                int swipedPosition = manager.getTopPosition() - 1;
                if (swipedPosition < 0 || swipedPosition >= prendas.size()) return;

                Prenda prenda = prendas.get(swipedPosition);

                if (direction == Direction.Right) {
                    // Evitar guardar duplicados
                    if (!savedOutfits.contains(prenda)) {
                        savePrendaToFavorites(prenda);
                        savedOutfits.add(prenda);
                        Toast.makeText(Transition.this, getString(R.string.añadido_favoritos), Toast.LENGTH_SHORT).show();
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

                            Prenda prenda = new Prenda(imagenUrl, talla, material, color, tipo);
                            prendas.add(prenda);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Error al cargar prendas", Toast.LENGTH_SHORT).show();
                        Log.e("Firestore", "Error al obtener prendas", task.getException());
                    }
                });
    }

    private void clearSavedOutfitsInFirestore() {
        CollectionReference outfitsRef = db.collection("users").document(userId).collection("saved_outfits");
        outfitsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    outfitsRef.document(document.getId()).delete();
                }
            } else {
                Log.e("Firestore", "Error al limpiar outfits", task.getException());
            }
        });
    }

    private void savePrendaToFavorites(Prenda prenda) {
        CollectionReference favRef = db.collection("users").document(userId).collection("favoritos");

        // Crear un id único para evitar duplicados en Firestore
        String docId = prenda.getTipo() + "_" + prenda.getImagenUrl().hashCode();

        Map<String, Object> prendaMap = new HashMap<>();
        prendaMap.put("imagenUrl", prenda.getImagenUrl());
        prendaMap.put("talla", prenda.getTalla());
        prendaMap.put("material", prenda.getMaterial());
        prendaMap.put("color", prenda.getColor());
        prendaMap.put("tipo", prenda.getTipo());

        favRef.document(docId).set(prendaMap)
                .addOnSuccessListener(docRef -> Log.d("Firestore", "Prenda guardada en favoritos"))
                .addOnFailureListener(e -> Log.e("Firestore", "Error al guardar prenda en favoritos", e));
    }
}
