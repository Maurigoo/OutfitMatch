package com.example.outfitmatch.modelo.persistencia;

public class DaoPrenda {

    private static DaoPrenda instance;

    private DaoPrenda(){
        super();
    }

    public static DaoPrenda getInstance() {
        return instance==null? instance= new DaoPrenda() : instance;
    }

}
