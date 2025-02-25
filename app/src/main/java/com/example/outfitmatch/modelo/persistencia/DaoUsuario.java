package com.example.outfitmatch.modelo.persistencia;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;

/**
 * DaoUsuario es una clase que gestiona las operaciones relacionadas
 * con la autenticación de usuarios en Firebase Authentication.
 * Implementa el patrón Singleton para asegurar una única instancia.
 */
public class DaoUsuario {

    private static DaoUsuario instance;  // Instancia singleton de DaoUsuario
    private FirebaseAuth mAuth;          // Instancia de FirebaseAuth para la autenticación

    /**
     * Constructor privado para aplicar el patrón Singleton.
     * Inicializa la instancia de FirebaseAuth.
     */
    private DaoUsuario() {
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * Obtiene la instancia única de DaoUsuario.
     *
     * @return Instancia singleton de DaoUsuario.
     */
    public static DaoUsuario getInstance() {
        return instance == null ? instance = new DaoUsuario() : instance;
    }

    /**
     * Inicia sesión con Firebase Authentication usando el correo electrónico y la contraseña.
     *
     * @param email    Correo electrónico del usuario.
     * @param password Contraseña del usuario.
     * @return Una Task<Void> que representa el resultado del inicio de sesión.
     *         - Si el inicio de sesión es exitoso, retorna una tarea completada.
     *         - Si ocurre un error, retorna una tarea fallida con la excepción.
     */
    public Task<Void> signIn(String email, String password) {
        return mAuth.signInWithEmailAndPassword(email, password).continueWithTask(task -> {
            if (task.isSuccessful()) {
                // Retorna una tarea completada si el inicio de sesión fue exitoso
                return Tasks.forResult(null);
            } else {
                // Lanza la excepción si el inicio de sesión falló
                throw task.getException();
            }
        });
    }
}
