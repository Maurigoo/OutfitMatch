package com.example.outfitmatch.modelo.persistencia;

import com.example.outfitmatch.modelo.entidad.Prenda;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class DaoPrenda {
    private static DaoPrenda instance;
    private StorageReference storageReference;
    private FirebaseFirestore firestore;

    private DaoPrenda() {
        storageReference = FirebaseStorage.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();
    }

    public static DaoPrenda getInstance() {
        if (instance == null) {
            instance = new DaoPrenda();
        }
        return instance;
    }

    /**
     * Sube una imagen a Firebase Storage.
     *
     * @param imagenUri URI de la imagen a subir.
     * @param userId ID del usuario al que pertenece la imagen.
     * @return UploadTask para monitorear el estado de la subida.
     */
    public UploadTask subirImagen(Uri imagenUri, String userId) {
        StorageReference fileRef = storageReference.child("images/" + userId + "/" + imagenUri.getLastPathSegment());
        return fileRef.putFile(imagenUri);
    }

    /**
     * Interface para callback al cargar prendas.
     */
    public interface OnPrendasLoadedListener{
        void onPrendasLoaded(List<Prenda> prendas);
    }

    /**
     * Obtiene las prendas almacenadas en Firebase Firestore.
     *
     * @param userId ID del usuario.
     * @param listener Callback que retorna la lista de prendas.
     */
    public void ObtenerPrendaFirebase(String userId, OnPrendasLoadedListener listener){
        firestore.collection("users").document(userId).collection("clothes")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Prenda> prendas = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String talla = doc.getString("talla");
                        String material = doc.getString("material");
                        String color = doc.getString("color");
                        int imageResource = 0; // Aquí puedes cargar la imagen desde URL si es necesario

                        prendas.add(new Prenda(imageResource, talla, material, color));
                    }
                    listener.onPrendasLoaded(prendas);
                });
    }
}
