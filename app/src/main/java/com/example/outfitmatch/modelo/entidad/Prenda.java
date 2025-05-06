package com.example.outfitmatch.modelo.entidad;

import java.io.Serializable;

public class Prenda implements Serializable {

    private int imagen; // Para recursos locales
    private String imagenUrl; // Para imágenes desde Firebase
    private String talla;
    private String material;
    private String color;
    private String tipo;

    // Constructor para recursos locales
    public Prenda(int imagen, String talla, String material, String color, String tipo) {
        this.imagen = imagen;
        this.talla = talla;
        this.material = material;
        this.color = color;
        this.tipo = tipo;
    }

    // Constructor para imágenes desde Firebase
    public Prenda(String imagenUrl, String talla, String material, String color, String tipo) {
        this.imagenUrl = imagenUrl;
        this.talla = talla;
        this.material = material;
        this.color = color;
        this.tipo = tipo;
    }

    // Getters y Setters
    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Prenda{" +
                "imagen=" + imagen +
                ", imagenUrl='" + imagenUrl + '\'' +
                ", talla='" + talla + '\'' +
                ", material='" + material + '\'' +
                ", color='" + color + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
