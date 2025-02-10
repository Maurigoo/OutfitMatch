package com.example.outfitmatch.modelo.persistencia;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import android.net.Uri;

public class DaoPrenda {
    private static DaoPrenda instance;
    private StorageReference storageReference;

    private DaoPrenda() {
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    public static DaoPrenda getInstance() {
        if (instance == null) {
            instance = new DaoPrenda();
        }
        return instance;
    }

    public UploadTask subirImagen(Uri imagenUri, String userId) {
        StorageReference fileRef = storageReference.child("images/" + userId + "/" + imagenUri.getLastPathSegment());
        return fileRef.putFile(imagenUri);
    }
}
