package com.example.outfitmatch.modelo.persistencia;

public class DaoUsuario {

    private static DaoUsuario instance;

    private DaoUsuario(){
        super();
    }

    public static DaoUsuario getInstance() {
        return instance==null? instance= new DaoUsuario() : instance;
    }


}
