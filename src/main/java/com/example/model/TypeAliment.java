package com.example.model;



public class TypeAliment {
    
    private int id; // L'identifiant unique du type d'aliment
    private String nom; // Le nom du type de l'aliment
   
    // Constructeur par défaut
    public TypeAliment() {
    }

    // Constructeur avec tous les attributs
       public TypeAliment( int id,String nom) {       
        this.id = id;
        this.nom = nom;
    }

    public TypeAliment( String nom ) {       
        this.nom = nom;
   
    }

    // Getters et Setters pour chaque attribut
    public int getId() {
        return id;
    }

    public void setId(int id2) {
        this.id = id2;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

   

    // Méthode toString pour afficher les informations du TypeAliment
    @Override
    public String toString() {
        return "TypeAliment{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                '}';
    }

 
}



