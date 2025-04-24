package com.example.outfitmatch.modelo.negocio;

import android.text.TextUtils;

import com.example.outfitmatch.modelo.entidad.Usuario;
import com.example.outfitmatch.modelo.persistencia.DaoUsuario;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

/**
 * GestorUsuario es una clase singleton responsable de gestionar la lógica relacionada
 * con los usuarios, como el inicio de sesión, validación de datos y recuperación de contraseña.
 */
public class GestorUsuario {

    private static GestorUsuario instance;  // Instancia singleton de GestorUsuario
    private DaoUsuario daoUsuario = DaoUsuario.getInstance();  // Acceso al DAO de Usuario

    /**
     * Constructor privado para aplicar el patrón Singleton.
     */
    private GestorUsuario() {
        super();
    }

    /**
     * Obtiene la instancia única de GestorUsuario.
     *
     * @return Instancia singleton de GestorUsuario.
     */
    public static GestorUsuario getInstance() {
        return instance == null ? instance = new GestorUsuario() : instance;
    }

    /**
     * Inicia sesión con el usuario proporcionado después de validar los datos.
     *
     * @param usuario Objeto Usuario que contiene el email y la contraseña.
     * @return Una Task<Void> que representa el resultado del inicio de sesión.
     */
    public Task<Void> iniciarSesion(Usuario usuario) {
        String validationError = validarUsuario(usuario);
        if (validationError != null) {
            return Tasks.forException(new Exception(validationError));
        }

        return daoUsuario.signIn(usuario.getEmail(), usuario.getPassword());
    }

    /**
     * Valida los datos del usuario antes de iniciar sesión.
     *
     * @param usuario Objeto Usuario a validar.
     * @return Un mensaje de error si la validación falla, o null si es válida.
     */
    private String validarUsuario(Usuario usuario) {
        if (TextUtils.isEmpty(usuario.getEmail())) {
            return "El correo electrónico es obligatorio";
        }

        if (TextUtils.isEmpty(usuario.getPassword())) {
            return "La contraseña es obligatoria";
        }

        if (usuario.getPassword().length() < 8) {
            return "La contraseña debe tener al menos 8 caracteres";
        }

        return null;  // Datos válidos
    }

    /**
     * Envía un correo electrónico para restablecer la contraseña del usuario.
     *
     * @param email Dirección de correo del usuario.
     * @return Task<Void> que indica si el correo fue enviado correctamente.
     */
    public Task<Void> enviarResetPassword(String email) {
        if (TextUtils.isEmpty(email)) {
            return Tasks.forException(new Exception("El correo electrónico no puede estar vacío"));
        }
        return daoUsuario.resetPassword(email);
    }
}
