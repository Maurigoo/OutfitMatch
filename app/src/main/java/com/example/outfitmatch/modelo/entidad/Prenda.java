package com.example.outfitmatch.modelo.entidad;

import java.util.List;

//de esta clase extienden luego camiseta, pantalon, zapatos ...
public class Prenda {

    private String talla;
    private String materiales;
    private String color;
    private int imagenResId;


    public Prenda(String talla, String materiales, String color, int imagenResId) {
        this.talla = talla;
        this.materiales = materiales;
        this.color = color;
        this.imagenResId = imagenResId;
    }

    public int getImagenResId() {
        return imagenResId;
    }

    public void setImagenResId(int imagenResId) {
        this.imagenResId = imagenResId;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public String getMateriales() {
        return materiales;
    }

    public void setMateriales(String materiales) {
        this.materiales = materiales;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Prenda{" +
                "talla='" + talla + '\'' +
                ", materiales=" + materiales +
                ", color='" + color + '\'' +
                '}';
    }
}
