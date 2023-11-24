package com.example.model;

public class Couleur {
    private int id;
    private String nom;
    private String hexadecimal_rvb;

    // Constructeur sans l'ID (puisqu'il est généré automatiquement pas la base de données )
    public Couleur(String nom, String hexadecimal_rvb) {
        this.nom = nom;
        this.hexadecimal_rvb = hexadecimal_rvb;
    }


     // Constructeur sans paramètre
    public Couleur() {

    }


    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nomCouleur) {
        this.nom = nomCouleur;
    }

    public String getHexadecimal_rvb() {
        return hexadecimal_rvb;
    }

    public void setHexadecimal_rvb(String hexadecimal_rvb) {
        this.hexadecimal_rvb = hexadecimal_rvb;
    }

}
