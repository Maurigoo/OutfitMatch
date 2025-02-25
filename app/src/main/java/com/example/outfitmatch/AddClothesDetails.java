package com.example.outfitmatch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.outfitmatch.modelo.entidad.Prenda;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * AddClothesDetails es una actividad donde el usuario puede agregar detalles a una prenda seleccionada,
 * como color, talla, material y tipo. La imagen se almacena en Firebase Storage y los datos en Firestore.
 */
public class AddClothesDetails extends AppCompatActivity {

    private ImageView imageViewSelected; // Vista previa de la imagen seleccionada
    private EditText editTextColor, editTextTalla, editTextMaterial; // Campos de entrada de datos
    private Spinner spinnerTipo; // Spinner para seleccionar el tipo de prenda
    private Button buttonUpload; // Botón para subir la prenda
    private Uri imageUri; // URI de la imagen seleccionada

    /**
     * Método llamado al crear la actividad. Inicializa la UI y configura los listeners.
     *
     * @param savedInstanceState Estado previamente guardado de la actividad (si aplica).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clothes_details);

        // Inicialización de vistas
        imageViewSelected = findViewById(R.id.imageViewSelected);
        editTextColor = findViewById(R.id.editTextColor);
        editTextTalla = findViewById(R.id.editTextTalla);
        editTextMaterial = findViewById(R.id.editTextMaterial);
        spinnerTipo = findViewById(R.id.spinnerTipo);
        buttonUpload = findViewById(R.id.buttonUpload);

        // Configuración del Spinner con layouts personalizados
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.tipo_prenda_array,        // Asegúrate de que este array está definido en strings.xml
                R.layout.spinner_item             // Layout personalizado para el Spinner cerrado
        );
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item); // Layout para los ítems desplegables
        spinnerTipo.setAdapter(adapter);

        // Obtener la URI de la imagen seleccionada
        imageUri = getIntent().getParcelableExtra("IMAGE_URI");
        Glide.with(this).load(imageUri).into(imageViewSelected);

        // Configurar el botón para subir la prenda
        buttonUpload.setOnClickListener(v -> uploadClothesToFirebase());
    }

    /**
     * Sube la prenda a Firebase Storage y guarda los datos en Firestore.
     */
    private void uploadClothesToFirebase() {
        String color = editTextColor.getText().toString().trim();
        String talla = editTextTalla.getText().toString().trim();
        String material = editTextMaterial.getText().toString().trim();
        String tipo = spinnerTipo.getSelectedItem().toString().trim();

        // Validación de campos
        if (color.isEmpty() || talla.isEmpty() || material.isEmpty() || tipo.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mostrar diálogo de progreso
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Subiendo prenda...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference fileRef = storageRef.child("images/" + tipo + "/" + System.currentTimeMillis() + ".jpg");

            // Subir la imagen a Firebase Storage
            fileRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();

                        // Crear objeto Prenda con los datos
                        Prenda prenda = new Prenda(0, talla, material, color, tipo);
                        prenda.setImagenUrl(imageUrl); // Establecer la URL de la imagen

                        // Guardar en Firestore
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        DocumentReference prendaRef = db.collection("prendas").document(userId)
                                .collection("user_prendas").document();

                        prendaRef.set(prenda)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(AddClothesDetails.this, "Prenda subida con éxito", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(AddClothesDetails.this, "Error al subir prenda", Toast.LENGTH_SHORT).show();
                                    }
                                    progressDialog.dismiss();
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(AddClothesDetails.this, "Error en Firestore", Toast.LENGTH_SHORT).show();
                                    Log.e("Firestore", "Error al guardar prenda", e);
                                    progressDialog.dismiss();
                                });

                    }))
                    .addOnFailureListener(e -> {
                        Toast.makeText(AddClothesDetails.this, "Error al subir imagen", Toast.LENGTH_SHORT).show();
                        Log.e("FirebaseStorage", "Error al subir imagen", e);
                        progressDialog.dismiss();
                    });
        }
    }
}
