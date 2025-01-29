package com.example.outfitmatch.modelo.persistencia;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;

public class DaoUsuario {

    private static DaoUsuario instance;
    private FirebaseAuth mAuth;

    private DaoUsuario() {
        mAuth = FirebaseAuth.getInstance();
    }

    public static DaoUsuario getInstance() {
        return instance == null ? instance = new DaoUsuario() : instance;
    }

    public Task<Void> signIn(String email, String password) {
        return mAuth.signInWithEmailAndPassword(email, password).continueWithTask(task -> {
            if (task.isSuccessful()) {
                return Tasks.forResult(null);
            } else {
                throw task.getException();
            }
        });
    }
}
