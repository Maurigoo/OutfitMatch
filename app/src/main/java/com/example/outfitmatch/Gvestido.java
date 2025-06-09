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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.outfitmatch.adaptador.AdaptadorDosPiezas;
import com.example.outfitmatch.modelo.entidad.Prenda;
import com.example.outfitmatch.modelo.negocio.GestorPrenda;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Gvestido extends AppCompatActivity {

    private RecyclerView rvDresses, rvJackets, rvAccessories, rvShoes;
    private Prenda dressSeleccionada, jacketSeleccionada, accessorySeleccionada, shoeSeleccionada;
    private FirebaseFirestore db;
    private String userId;
    private Button btnGuardarOutfit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.setStatusBarColor(Color.TRANSPARENT);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gvestido);

        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        rvDresses = findViewById(R.id.recyclerdresses);
        rvJackets = findViewById(R.id.recyclerjacket);
        rvAccessories = findViewById(R.id.recycleraccsessories);
        rvShoes = findViewById(R.id.recyclerShoes);
        btnGuardarOutfit = findViewById(R.id.btnGuardarOutfit);

        configurarRecycler(rvDresses);
        configurarRecycler(rvJackets);
        configurarRecycler(rvAccessories);
        configurarRecycler(rvShoes);

        cargarPrendas();

        btnGuardarOutfit.setOnClickListener(v -> guardarOutfit());
    }

    private void configurarRecycler(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void cargarPrendas() {
        GestorPrenda.getInstance().obtenerPrendasSoloFirebase(userId, new GestorPrenda.OnTotalPrendasListener() {
            @Override
            public void onTotalPrendas(int total, List<Prenda> prendas) {
                List<Prenda> dresses = new ArrayList<>();
                List<Prenda> jackets = new ArrayList<>();
                List<Prenda> accessories = new ArrayList<>();
                List<Prenda> shoes = new ArrayList<>();

                for (Prenda p : prendas) {
                    if (p.getTipo() == null) continue;
                    String tipo = p.getTipo().toLowerCase().trim();
                    Log.d("Gvestido", "Prenda tipo: '" + p.getTipo() + "'");

                    switch (tipo) {
                        case "dress":
                        case "dresses":
                        case "vestido":
                        case "vestidos":
                            dresses.add(p);
                            break;

                        case "jacket":
                        case "jackets":
                        case "chaqueta":
                        case "chaquetas":
                            jackets.add(p);
                            break;

                        case "accessory":
                        case "accessories":
                        case "accesorio":
                        case "accesorios":
                            accessories.add(p);
                            break;

                        case "shoe":
                        case "shoes":
                        case "zapato":
                        case "zapatos":
                            shoes.add(p);
                            break;

                        default:
                            Log.w("Gvestido", "Tipo no reconocido: '" + p.getTipo() + "'");
                            break;
                    }
                }

                rvDresses.setAdapter(new AdaptadorDosPiezas(Gvestido.this, dresses, p -> dressSeleccionada = p));
                rvJackets.setAdapter(new AdaptadorDosPiezas(Gvestido.this, jackets, p -> jacketSeleccionada = p));
                rvAccessories.setAdapter(new AdaptadorDosPiezas(Gvestido.this, accessories, p -> accessorySeleccionada = p));
                rvShoes.setAdapter(new AdaptadorDosPiezas(Gvestido.this, shoes, p -> shoeSeleccionada = p));
            }

            @Override
            public void onError(@NonNull Exception e) {
                Toast.makeText(Gvestido.this, "Error cargando prendas: " + e.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("Gvestido", "Error al cargar prendas", e);
            }
        });
    }

    private void guardarOutfit() {
        if (dressSeleccionada == null || jacketSeleccionada == null || accessorySeleccionada == null || shoeSeleccionada == null) {
            Toast.makeText(this, "Selecciona una prenda de cada categoría.", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Prenda> outfit = new ArrayList<>();
        outfit.add(dressSeleccionada);
        outfit.add(jacketSeleccionada);
        outfit.add(accessorySeleccionada);
        outfit.add(shoeSeleccionada);

        Outfit nuevoOutfit = new Outfit(outfit);

        // Guardar en historial
        db.collection("users").document(userId).collection("saved_outfits")
                .add(nuevoOutfit)
                .addOnSuccessListener(docRef -> Log.d("Gvestido", "Outfit guardado en historial"))
                .addOnFailureListener(e -> Log.e("Gvestido", "Error guardando historial", e));

        // Guardar como outfit actual
        db.collection("users").document(userId)
                .collection("current_outfit").document("outfit_actual")
                .set(nuevoOutfit)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(Gvestido.this, "Outfit guardado", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Gvestido.this, Outfits.class));
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(Gvestido.this, "Error al guardar outfit actual", Toast.LENGTH_SHORT).show());
    }

    public static class Outfit {
        public List<Prenda> prendas;
        public Outfit(List<Prenda> prendas) {
            this.prendas = prendas;
        }
    }
}
