package com.example.outfitmatch.modelo.entidad;

import java.util.List;

//de esta clase extienden luego camiseta, pantalon, zapatos ...
public class Prenda {

    private String talla;
    private List<Material> materiales;//he pensado q una prenda tiene mas de un material pero si quereis
                                        //para simplificarlo lo dejamos sin array list
    private String color;

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public List<Material> getMateriales() {
        return materiales;
    }

    public void setMateriales(List<Material> materiales) {
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
