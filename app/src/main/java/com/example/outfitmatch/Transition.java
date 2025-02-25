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
import com.google.firebase.firestore.CollectionReference;
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

/**
 * Transition es una actividad que permite al usuario navegar por una lista de prendas usando
 * una interfaz estilo Tinder. Las prendas pueden ser deslizadas hacia la derecha para guardarlas
 * como favoritas o hacia la izquierda para descartarlas.
 *
 * Los outfits seleccionados se almacenan en Firestore y pueden ser visualizados en la actividad Outfits.
 */
public class Transition extends AppCompatActivity {

    private Button like, x, outfit;                        // Botones de interacción
    private CardStackView cardStackView;                   // Vista para las tarjetas deslizables
    private CardStackLayoutManager manager;                // Gestor de las tarjetas
    private AdaptadorTransition adapter;                   // Adaptador para mostrar las prendas

    private List<Prenda> savedOutfits = new ArrayList<>(); // Lista de prendas seleccionadas
    private List<Prenda> prendas = new ArrayList<>();      // Lista de todas las prendas

    private FirebaseFirestore db;                          // Instancia de Firestore
    private String userId;                                 // ID del usuario autenticado

    /**
     * Método llamado al crear la actividad. Inicializa Firestore, la vista y carga datos.
     *
     * @param savedInstanceState Estado previamente guardado de la actividad (si aplica).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);

        // Inicialización de Firestore y autenticación
        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Limpiar outfits guardados previamente
        clearSavedOutfitsInFirestore();

        // Configurar navegación inferior
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

        // Inicializar vistas
        like = findViewById(R.id.botonLike);
        x = findViewById(R.id.botonX);
        outfit = findViewById(R.id.botonOufits);
        cardStackView = findViewById(R.id.cardStackView);

        // Configurar la vista de tarjetas y cargar prendas
        setupCardStackView();
        loadPrendasFromFirestore();

        // Guardar outfits seleccionados y navegar a Outfits
        outfit.setOnClickListener(view -> {
            saveOutfitsToFirestore();
            Intent intent = new Intent(Transition.this, Outfits.class);
            startActivity(intent);
        });

        // Configurar botones de like y dislike
        like.setOnClickListener(view -> swipeCard(Direction.Right));
        x.setOnClickListener(view -> swipeCard(Direction.Left));
    }

    /**
     * Ejecuta la animación para deslizar la tarjeta en la dirección indicada.
     *
     * @param direction Dirección hacia donde se desliza la tarjeta.
     */
    private void swipeCard(Direction direction) {
        SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                .setDirection(direction).setDuration(200).build();
        manager.setSwipeAnimationSetting(setting);
        cardStackView.swipe();
    }

    /**
     * Configura el CardStackView para mostrar las tarjetas de prendas.
     */
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

        // Configuración visual del CardStack
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

    /**
     * Carga las prendas del usuario desde Firestore y las muestra en el CardStackView.
     */
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

                            Prenda prenda = new Prenda(0, talla, material, color, tipo);
                            prenda.setImagenUrl(imagenUrl);

                            prendas.add(prenda);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Error al cargar prendas", Toast.LENGTH_SHORT).show();
                        Log.e("Firestore", "Error al obtener prendas", task.getException());
                    }
                });
    }

    /**
     * Guarda los outfits seleccionados en Firestore bajo la colección 'saved_outfits'.
     */
    private void saveOutfitsToFirestore() {
        CollectionReference outfitsRef = db.collection("users").document(userId).collection("saved_outfits");

        for (Prenda prenda : savedOutfits) {
            Map<String, Object> prendaMap = new HashMap<>();
            prendaMap.put("imagenUrl", prenda.getImagenUrl());
            prendaMap.put("talla", prenda.getTalla());
            prendaMap.put("material", prenda.getMaterial());
            prendaMap.put("color", prenda.getColor());
            prendaMap.put("tipo", prenda.getTipo());

            outfitsRef.add(prendaMap)
                    .addOnSuccessListener(documentReference -> Log.d("Firestore", "Prenda guardada en outfits"))
                    .addOnFailureListener(e -> Log.e("Firestore", "Error al guardar prenda en outfits", e));
        }
    }

    /**
     * Elimina los outfits guardados en Firestore al iniciar la actividad para permitir una nueva selección.
     */
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
}
