package com.example.outfitmatch;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.outfitmatch.adaptador.AdaptadorClothes;
import com.example.outfitmatch.modelo.entidad.Prenda;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Actividad que muestra una lista de prendas que el usuario ha cargado.
 * Las prendas se obtienen de Firebase Firestore y se muestran en un RecyclerView.
 */
public class ClothesListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdaptadorClothes clothesAdapter;
    private List<Prenda> clothesList;
    private FirebaseFirestore db;

    /**
     * Método que se ejecuta cuando se crea la actividad. Inicializa el RecyclerView,
     * el adaptador y carga las prendas desde Firestore.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes_list);

        // Inicializar el RecyclerView y su adaptador
        recyclerView = findViewById(R.id.recyclerViewClothes);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // Dos columnas en la cuadrícula

        clothesList = new ArrayList<>();
        clothesAdapter = new AdaptadorClothes(clothesList, prenda ->
                // Acción al seleccionar una prenda
                Toast.makeText(this, "Seleccionaste: " + prenda.getTalla(), Toast.LENGTH_SHORT).show()
        );
        recyclerView.setAdapter(clothesAdapter);

        db = FirebaseFirestore.getInstance(); // Obtener la instancia de Firebase Firestore

        // Obtener la categoría seleccionada desde el Intent
        String category = getIntent().getStringExtra("CATEGORY");

        if (category != null) {
            // Si la categoría es válida, cargar las prendas desde Firestore
            loadClothesFromFirestore(category);
        } else {
            // Si no se seleccionó categoría, mostrar un mensaje
            Toast.makeText(this, "No category selected", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Método que carga las prendas desde Firestore para una categoría específica.
     * @param category La categoría de las prendas a cargar (por ejemplo, "All", "Shirts", etc.).
     */
    @SuppressLint("NotifyDataSetChanged")
    private void loadClothesFromFirestore(String category) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Obtener el ID del usuario

        // Realizar la consulta en la colección 'prendas' de Firestore para el usuario actual
        db.collection("prendas").document(userId).collection("user_prendas")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        clothesList.clear(); // Limpiar la lista de prendas

                        // Iterar sobre los documentos obtenidos
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String tipo = document.getString("tipo");

                            // Filtrar según la categoría seleccionada
                            if (category.equals("All") || category.equals(tipo)) {
                                // Manejo seguro del campo 'imagen' en el documento
                                String imagenUrl = document.getString("imagenUrl");
                                if (document.contains("imagen")) {
                                    Object imagenObj = document.get("imagen");

                                    // Comprobar si la imagen es un String, Map o List y obtener la URL correspondiente
                                    if (imagenObj instanceof String) {
                                        imagenUrl = (String) imagenObj;
                                    } else if (imagenObj instanceof Map) {
                                        Map<String, Object> imagenMap = (Map<String, Object>) imagenObj;
                                        imagenUrl = (String) imagenMap.get("imagenUrl"); // Asegúrate de que la clave "imagenUrl" existe
                                    } else if (imagenObj instanceof List) {
                                        List<String> imagenList = (List<String>) imagenObj;
                                        if (!imagenList.isEmpty()) {
                                            imagenUrl = imagenList.get(0); // Si es una lista de URLs, tomar la primera
                                        }
                                    }
                                }

                                // Obtener otros datos de la prenda (talla, material, color, tipo)
                                String talla = document.getString("talla");
                                String material = document.getString("material");
                                String color = document.getString("color");

                                // Crear una nueva instancia de Prenda y agregarla a la lista
                                Prenda prenda = new Prenda(0, talla, material, color, tipo);
                                if (imagenUrl != null) {
                                    prenda.setImagenUrl(imagenUrl); // Establecer la URL de la imagen
                                }
                                clothesList.add(prenda); // Agregar la prenda a la lista
                            }
                        }

                        // Notificar al adaptador para que actualice la vista
                        clothesAdapter.notifyDataSetChanged();
                    } else {
                        // Si hay un error al cargar las prendas, mostrar un mensaje
                        Toast.makeText(this, "Error al cargar prendas", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
