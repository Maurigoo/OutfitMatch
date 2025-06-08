package com.example.outfitmatch.modelo.entidad;

public class Outfit {
    private int shirt;
    private int pant;
    private int shoe;

    public Outfit(int shirt, int pant, int shoe) {
        this.shirt = shirt;
        this.pant = pant;
        this.shoe = shoe;
    }

    public int getShirt() { return shirt; }
    public int getPant() { return pant; }
    public int getShoe() { return shoe; }
}
