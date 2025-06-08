package com.example.outfitmatch;

import android.util.Log;

import com.example.outfitmatch.modelo.entidad.Prenda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OutfitGenerator {

    public static List<List<Prenda>> generateOutfits(List<Prenda> prendas) {
        // Separar prendas por tipo
        Map<String, List<Prenda>> prendasPorTipo = new HashMap<>();
        for (Prenda prenda : prendas) {
            String tipo = prenda.getTipo().trim().toLowerCase(); // Normalizar a minúsculas
            if (tipo.endsWith("s")) {
                tipo = tipo.substring(0, tipo.length() - 1); // Singularizar
            }
            prendasPorTipo.putIfAbsent(tipo, new ArrayList<>());
            prendasPorTipo.get(tipo).add(prenda);
        }

        // Categorías
        List<Prenda> tops = prendasPorTipo.getOrDefault("shirt", new ArrayList<>());
        List<Prenda> pants = prendasPorTipo.getOrDefault("pant", new ArrayList<>());
        List<Prenda> shoes = prendasPorTipo.getOrDefault("shoe", new ArrayList<>());
        List<Prenda> accessories = prendasPorTipo.getOrDefault("accessorie", new ArrayList<>());
        List<Prenda> dresses = prendasPorTipo.getOrDefault("dresse", new ArrayList<>()); // Cambiado a "dress"

        // Logs para verificar las prendas
        Log.d("PrendasLog", "Categoría 'shirt' contiene: " + tops.size() + " prendas: " + tops);
        Log.d("PrendasLog", "Categoría 'pant' contiene: " + pants.size() + " prendas: " + pants);
        Log.d("PrendasLog", "Categoría 'shoe' contiene: " + shoes.size() + " prendas: " + shoes);
        Log.d("PrendasLog", "Categoría 'accessory' contiene: " + accessories.size() + " prendas: " + accessories);
        Log.d("PrendasLog", "Categoría 'dress' contiene: " + dresses.size() + " prendas: " + dresses);

        if ((tops.isEmpty() || pants.isEmpty() || shoes.isEmpty()) && dresses.isEmpty()) {
            throw new IllegalArgumentException("Faltan prendas en las categorías obligatorias: 'shirt', 'pant', 'shoe' o 'dresses'.");
        }

        // Usar un conjunto para evitar duplicados
        Set<List<Prenda>> uniqueOutfits = new HashSet<>();

        // Generar outfits con tops, pants, y shoes
        for (Prenda top : tops) {
            for (Prenda pant : pants) {
                for (Prenda shoe : shoes) {
                    if (!accessories.isEmpty()) {
                        for (Prenda accessory : accessories) {
                            List<Prenda> outfit = new ArrayList<>();
                            outfit.add(top);
                            outfit.add(pant);
                            outfit.add(shoe);
                            outfit.add(accessory);
                            uniqueOutfits.add(outfit);
                        }
                    } else {
                        List<Prenda> outfit = new ArrayList<>();
                        outfit.add(top);
                        outfit.add(pant);
                        outfit.add(shoe);
                        uniqueOutfits.add(outfit);
                    }
                }
            }
        }


// Agregar logs para validación
        Log.d("PrendasPorTipo", "Contenido de prendasPorTipo: " + prendasPorTipo);

        // Generar outfits con dresses
        for (Prenda dress : dresses) {
            if (!accessories.isEmpty()) {
                for (Prenda accessory : accessories) {
                    List<Prenda> outfit = new ArrayList<>();
                    outfit.add(dress);
                    outfit.add(accessory);
                    uniqueOutfits.add(outfit);
                }
            } else {
                List<Prenda> outfit = new ArrayList<>();
                outfit.add(dress);
                uniqueOutfits.add(outfit);
            }
        }


        // Convertir el conjunto a lista para devolverlo
        List<List<Prenda>> outfits = new ArrayList<>(uniqueOutfits);

        // Log de outfits generados
        Log.d("OutfitsLog", "Se generaron " + outfits.size() + " outfits únicos");
        for (List<Prenda> outfit : outfits) {
            Log.d("GeneratedOutfit", "Outfit único: " + outfit);
        }

        return outfits;
    }


}
