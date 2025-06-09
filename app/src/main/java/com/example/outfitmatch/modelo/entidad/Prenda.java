package com.example.outfitmatch.modelo.entidad;

import java.io.Serializable;
import java.util.Objects;

public class Prenda implements Serializable{
    private String id;      // <-- ID del documento en Firestore
    private String talla;
    private String material;
    private String color;
    private String tipo;
    private String imagenUrl;

    // Constructor vacío (requerido por Firestore)
    public Prenda() {}

    // Constructor con parámetros (sin id, porque se genera)
    public Prenda(String talla, String material, String color, String tipo) {
        this.talla = talla;
        this.material = material;
        this.color = color;
        this.tipo = tipo;
    }

    public Prenda(int id, String talla, String material, String color, String tipo) {
        this.id = String.valueOf(id); // Convierte el número a una cadena para Firestore
        this.talla = talla;
        this.material = material;
        this.color = color;
        this.tipo = tipo;
    }


    // Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
}
