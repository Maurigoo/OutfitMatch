package com.example.outfitmatch.modelo.negocio;

import android.text.TextUtils;

import com.example.outfitmatch.modelo.entidad.Usuario;
import com.example.outfitmatch.modelo.persistencia.DaoUsuario;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

public class GestorUsuario {

    private static GestorUsuario instance;
    private DaoUsuario daoUsuario = DaoUsuario.getInstance();

    private GestorUsuario() {
        super();
    }

    public static GestorUsuario getInstance() {
        return instance == null ? instance = new GestorUsuario() : instance;
    }

    public Task<Void> iniciarSesion(Usuario usuario) {
        String validationError = validarUsuario(usuario);
        if (validationError != null) {
            return Tasks.forException(new Exception(validationError));  // Correcto, crea una tarea fallida con la excepción
        }

        return daoUsuario.signIn(usuario.getEmail(), usuario.getPassword());
    }

    private String validarUsuario(Usuario usuario) {
        if (TextUtils.isEmpty(usuario.getEmail())) {
            return "El correo electrónico es obligatorio";
        }

        if (TextUtils.isEmpty(usuario.getPassword())) {
            return "La contraseña es obligatoria";
        }

        if (usuario.getPassword().length() < 6) {
            return "La contraseña debe tener al menos 6 caracteres";
        }

        return null;
    }
}
