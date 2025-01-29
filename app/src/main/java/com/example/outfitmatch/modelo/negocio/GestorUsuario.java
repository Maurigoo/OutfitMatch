    package com.example.outfitmatch.modelo.negocio;

    import com.example.outfitmatch.modelo.persistencia.DaoUsuario;

    public class GestorUsuario {

        private static GestorUsuario instance;

        private GestorUsuario(){
            super();
        }

        public static GestorUsuario getInstance() {
            return instance==null? instance= new GestorUsuario() : instance;
        }
    }
