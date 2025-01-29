package com.example.outfitmatch.modelo.negocio;

import com.example.outfitmatch.modelo.persistencia.DaoPrenda;

public class GestorPrenda {

    private static GestorPrenda instance;

    private GestorPrenda(){
        super();
    }

    public static GestorPrenda getInstance() {
        return instance==null? instance= new GestorPrenda() : instance;
    }
}
