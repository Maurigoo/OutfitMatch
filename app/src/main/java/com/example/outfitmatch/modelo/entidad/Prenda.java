package com.example.outfitmatch.modelo.entidad;

import java.io.Serializable;

public class Prenda implements Serializable {
    private int imageResource;
    private String talla;
    private String material;
    private String color;

    public Prenda(int imageResource, String talla, String material, String color) {
        this.imageResource = imageResource;
        this.talla = talla;
        this.material = material;
        this.color = color;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getTalla() {
        return talla;
    }

    public String getMaterial() {
        return material;
    }

    public String getColor() {
        return color;
    }
}