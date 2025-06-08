package com.example.outfitmatch;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import android.app.AlertDialog;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import me.ibrahimsn.lib.SmoothBottomBar;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Position;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.xml.KonfettiView;

import com.google.android.material.snackbar.Snackbar;


public class Transition extends AppCompatActivity {
    private AlertDialog generatingDialog;

    private Button like, x, favorito;
    private CardStackView cardStackView;
    private CardStackLayoutManager manager;
    private AdaptadorTransition adapter;

    private List<Prenda> savedOutfits = new ArrayList<>();
    private List<Prenda> prendas = new ArrayList<>();

    private FirebaseFirestore db;
    private String userId;

    private SmoothBottomBar bottomBar;
    private List<List<Prenda>> outfits = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);

        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        bottomBar = findViewById(R.id.bottomBar);
        like = findViewById(R.id.botonLike);
        x = findViewById(R.id.botonX);
        favorito = findViewById(R.id.botonfavorito);
        cardStackView = findViewById(R.id.cardStackView);

        setupCardStackView();
        loadPrendasFromFirestore(); // Carga las prendas desde Firestore

        // Botón para generar outfits (aunq ya se puede hacer automático)
        Button generateOutfitsButton = findViewById(R.id.botonGenerarOutfits);
       generateOutfitsButton.setOnClickListener(view -> generateAndDisplayOutfits());

        favorito.setOnClickListener(view -> {
            Intent intent = new Intent(Transition.this, Favorito.class);
            startActivity(intent);
        });

        like.setOnClickListener(view -> swipeCard(Direction.Right));
        x.setOnClickListener(view -> swipeCard(Direction.Left));

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
    private void showGeneratingDialog() {
        // Infla el layout del diálogo personalizado
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_generating_outfits, null);

        // Configura el AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setCancelable(false); // Evita que se cierre accidentalmente

        generatingDialog = builder.create();
        generatingDialog.show();
    }

    private void dismissGeneratingDialog() {
        if (generatingDialog != null && generatingDialog.isShowing()) {
            generatingDialog.dismiss();
        }
    }

    private void generateAndDisplayOutfits() {
        if (prendas.isEmpty()) {
            Toast.makeText(this, "No hay prendas suficientes para generar outfits", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mostrar el diálogo de carga
        Dialog loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.dialog_generating_outfits);
        loadingDialog.setCancelable(false);
        loadingDialog.show();

        // Simular la generación con un pequeño retraso
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            OutfitGenerator generator = new OutfitGenerator();
            outfits = generator.generateOutfits(prendas);

            loadingDialog.dismiss(); // Cerrar el diálogo cuando termine el proceso

            if (outfits.isEmpty()) {
                Snackbar.make(cardStackView, "No se pudieron generar outfits", Snackbar.LENGTH_LONG).show();
            } else {
                adapter = new AdaptadorTransition(outfits);
                cardStackView.setAdapter(adapter);

                // Mostrar el Snackbar con el número de outfits generados
                Snackbar snackbar = Snackbar.make(cardStackView, outfits.size() + " outfits generados", Snackbar.LENGTH_LONG);
                snackbar.setAction("OK", v -> {
                    // Acción al pulsar "OK" (puedes dejarlo vacío si no necesitas lógica adicional)
                });
                snackbar.show();

                // Mostrar el confeti
                KonfettiView konfettiView = findViewById(R.id.konfettiView);

                EmitterConfig emitterConfig = new Emitter(300, TimeUnit.MILLISECONDS).max(100); // Configurar el emisor
                konfettiView.start(
                        new PartyFactory(emitterConfig) // Usar el EmitterConfig
                                .angle(270) // Confeti hacia abajo
                                .spread(360)
                                .colors(Arrays.asList(Color.YELLOW, Color.GREEN, Color.MAGENTA)) // Lista de colores
                                .setSpeedBetween(5f, 15f)
                                .timeToLive(3000)
                                .position(new Position.Relative(0.5, 0.0)) // Desde el centro superior
                                .build()
                );
            }
        }, 2000); // Retraso simulado de 2 segundos (ajusta según sea necesario)
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

        // Inicializar adaptador con lista vacía de outfits para evitar error de tipo
        adapter = new AdaptadorTransition(new ArrayList<>());
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);
    }


    private void loadPrendasFromFirestore() {
        Log.d("Firestore", "Iniciando carga de prendas...");
        db.collection("prendas").document(userId).collection("user_prendas")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        prendas.clear();
                        Log.d("Firestore", "Consulta exitosa");

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("Firestore", "Documento leído: " + document.getId());

                            // Leer campos con validación de tipos
                            String imagenUrl = document.getString("imagenUrl");
                            String talla = document.getString("talla");
                            String material = document.getString("material");
                            String color = document.getString("color");
                            String tipo = document.getString("tipo");

                            // Validar si el campo imagen es un número y convertirlo a String
                            Object imagenObj = document.get("imagen");
                            String imagen = (imagenObj instanceof Number) ? String.valueOf(imagenObj) : (String) imagenObj;


                            // Verificar datos completos
                            if (imagenUrl != null && talla != null && material != null && color != null && tipo != null) {
                                Prenda prenda = new Prenda(imagenUrl, talla, material, color, tipo);
                                prendas.add(prenda);
                            } else {
                                Log.w("Firestore", "Documento incompleto: " + document.getData());
                            }
                        }

                        Log.d("Firestore", "Total prendas cargadas: " + prendas.size());
                        if (prendas.isEmpty()) {
                            Toast.makeText(this, "No se encontraron prendas", Toast.LENGTH_SHORT).show();
                        } else {
                            adapter.notifyDataSetChanged();
                            generateAndDisplayOutfits();
                        }
                    } else {
                        Toast.makeText(this, "Error al cargar prendas", Toast.LENGTH_SHORT).show();
                        Log.e("Firestore", "Error al obtener prendas", task.getException());
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
