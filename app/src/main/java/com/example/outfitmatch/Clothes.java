package com.example.outfitmatch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.outfitmatch.modelo.entidad.Prenda;
import com.example.outfitmatch.modelo.negocio.GestorPrenda;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

/**
 * Actividad que muestra las prendas del usuario y permite navegar por diferentes categorías.
 *
 * La clase gestiona la visualización de las prendas del usuario, y ofrece una interfaz con botones para
 * filtrar por categorías. Además, contiene un menú de navegación inferior que permite la navegación
 * a otras pantallas de la aplicación.
 */
public class Clothes extends AppCompatActivity {

    private TextView totalPrendasText;

    /**
     * Método que se ejecuta cuando se crea la actividad. Inicializa la interfaz de usuario
     * y configura la obtención de las prendas del usuario.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes);

        // Inicializar el TextView para mostrar el total de prendas
        totalPrendasText = findViewById(R.id.totalPrendasText);

        // Obtener el UID del usuario actual
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid(); // Obtener el UID

            // Llamar al método para obtener las prendas en tiempo real
            GestorPrenda.getInstance().obtenerPrendasSoloFirebase(userId, new GestorPrenda.OnTotalPrendasListener() {
                @Override
                public void onTotalPrendas(int total, List<Prenda> prendas) {
                    // Aquí obtienes el total de prendas de Firebase
                    Log.d("PrendasFirebase", "Tienes " + total + " prendas en Firebase.");

                    // Actualizar el TextView con el total de prendas
                    totalPrendasText.setText("Tienes " + total + " prendas :)");
                }

                @Override
                public void onError(@NonNull Exception e) {
                    // Manejo de errores al obtener prendas
                    Log.e("PrendasFirebase", "Error al obtener prendas: " + e.getMessage());
                    totalPrendasText.setText("Error al cargar las prendas :(");
                    Toast.makeText(Clothes.this, "Error al cargar las prendas: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Log.e("AuthError", "Usuario no autenticado");
            totalPrendasText.setText("No estás autenticado.");
            Toast.makeText(this, "Por favor, inicia sesión para ver tus prendas.", Toast.LENGTH_SHORT).show();
        }

        // Configurar los botones de categorías y la navegación inferior
        configurarBotones();
        configurarNavegacionInferior();
    }

    /**
     * Configura los botones de categorías de ropa. Cada botón al ser presionado
     * abre una nueva actividad con la lista de prendas correspondientes a la categoría seleccionada.
     */
    private void configurarBotones() {
        // Inicialización de los botones de categorías
        ImageButton botonShirts = findViewById(R.id.botonShirts);
        ImageButton botonPants = findViewById(R.id.botonPants);
        ImageButton botonShoes = findViewById(R.id.botonShoes);
        ImageButton botonDresses = findViewById(R.id.botonDresses);
        ImageButton botonAccessories = findViewById(R.id.botonAccessories);
        ImageButton botonAll = findViewById(R.id.botonAll);

        // Configuración de los listeners de cada botón
        botonShirts.setOnClickListener(view -> openClothesListActivity("Shirts"));
        botonPants.setOnClickListener(view -> openClothesListActivity("Pants"));
        botonShoes.setOnClickListener(view -> openClothesListActivity("Shoes"));
        botonDresses.setOnClickListener(view -> openClothesListActivity("Dresses"));
        botonAccessories.setOnClickListener(view -> openClothesListActivity("Accessories"));
        botonAll.setOnClickListener(view -> openClothesListActivity("All"));
    }

    /**
     * Configura la barra de navegación inferior para permitir la navegación entre pantallas principales
     * de la aplicación (Home, Clothes, Add y Profile).
     */
    private void configurarNavegacionInferior() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.boton_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_clothes);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                // Navegar según la opción seleccionada
                if (itemId == R.id.nav_home) {
                    startActivity(new Intent(getApplicationContext(), Home.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    return true;
                } else if (itemId == R.id.nav_clothes) {
                    return true;
                } else if (itemId == R.id.nav_add) {
                    startActivity(new Intent(getApplicationContext(), AddClothesAlbum.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(getApplicationContext(), Perfil.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * Abre la actividad de lista de prendas según la categoría seleccionada.
     *
     * @param category Categoría de ropa a mostrar en la lista.
     */
    private void openClothesListActivity(String category) {
        Intent intent = new Intent(Clothes.this, ClothesListActivity.class);
        intent.putExtra("CATEGORY", category);
        startActivity(intent);
    }
}
