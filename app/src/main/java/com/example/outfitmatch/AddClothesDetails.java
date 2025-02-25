package com.example.outfitmatch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.outfitmatch.modelo.entidad.Prenda;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class AddClothesDetails extends AppCompatActivity {

    private ImageView imageViewSelected;
    private EditText editTextColor, editTextTalla, editTextMaterial;
    private Spinner spinnerTipo;
    private Button buttonUpload;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clothes_details);

        imageViewSelected = findViewById(R.id.imageViewSelected);
        editTextColor = findViewById(R.id.editTextColor);
        editTextTalla = findViewById(R.id.editTextTalla);
        editTextMaterial = findViewById(R.id.editTextMaterial);
        spinnerTipo = findViewById(R.id.spinnerTipo);
        buttonUpload = findViewById(R.id.buttonUpload);

        // Configuración del Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tipo_prenda_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapter);

        // Obtener la URI de la imagen seleccionada
        imageUri = getIntent().getParcelableExtra("IMAGE_URI");
        Glide.with(this).load(imageUri).into(imageViewSelected);

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadClothesToFirebase();
            }
        });
    }

    private void uploadClothesToFirebase() {
        String color = editTextColor.getText().toString();
        String talla = editTextTalla.getText().toString();
        String material = editTextMaterial.getText().toString();
        String tipo = spinnerTipo.getSelectedItem().toString();

        if (color.isEmpty() || talla.isEmpty() || material.isEmpty() || tipo.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Subiendo prenda...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference fileRef = storageRef.child("images/" + tipo + "/" +   System.currentTimeMillis() + ".jpg");

            fileRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();

                        // Crear objeto Prenda
                        Prenda prenda = new Prenda(0, talla, material, color, tipo);

                        // Guardar en Firestore
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        DocumentReference prendaRef = db.collection("prendas").document(userId).collection("user_prendas").document();

                        Map<String, Object> prendaMap = new HashMap<>();
                        prendaMap.put("imagen", imageUrl);
                        prendaMap.put("talla", talla);
                        prendaMap.put("material", material);
                        prendaMap.put("color", color);
                        prendaMap.put("tipo", tipo);

                        prendaRef.set(prendaMap)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(AddClothesDetails.this, "Prenda subida con éxito", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(AddClothesDetails.this, "Error al subir prenda", Toast.LENGTH_SHORT).show();
                                    }
                                    progressDialog.dismiss();
                                    finish();
                                });

                    }))
                    .addOnFailureListener(e -> {
                        Toast.makeText(AddClothesDetails.this, "Error al subir imagen", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    });
        }
    }
}
