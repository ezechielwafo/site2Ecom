package org.example.Model.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Article implements EstIdentifiable, Serializable {
    private static final long serialVersionUID = 1L;

    private final long id;
    private String nom;
    private String description;
    private double prix;
    private int quantiteStock;
    private LocalDateTime dateMiseAJour;

    // Constructeur pour un nouvel article (l'ID sera géré par le DAO)
    public Article(String nom, String description, double prix, int quantiteStock) {
        // L'ID final sera attribué lors de la création par le DAO
        this.id = -1;
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.quantiteStock = quantiteStock;
        this.dateMiseAJour = LocalDateTime.now();
    }

    // Constructeur utilisé par le DAO pour charger/créer un objet avec un ID existant
    public Article(long id, String nom, String description, double prix, int quantiteStock) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.quantiteStock = quantiteStock;
        this.dateMiseAJour = LocalDateTime.now();
    }

    // Getters et Setters...
    @Override
    public long getId() { return id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; this.dateMiseAJour = LocalDateTime.now(); }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; this.dateMiseAJour = LocalDateTime.now(); }
    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; this.dateMiseAJour = LocalDateTime.now(); }
    public int getQuantiteStock() { return quantiteStock; }
    public void setQuantiteStock(int quantiteStock) { this.quantiteStock = quantiteStock; this.dateMiseAJour = LocalDateTime.now(); }
    public LocalDateTime getDateMiseAJour() { return dateMiseAJour; }

    @Override
    public String toString() {
        return String.format("[%d] %s (%.2f €) - Stock: %d", id, nom, prix, quantiteStock);
    }
}