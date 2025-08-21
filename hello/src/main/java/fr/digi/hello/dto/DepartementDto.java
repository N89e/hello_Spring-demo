package fr.digi.hello.dto;

public class DepartementDto {
    private Integer id;  // facultatif, selon besoin
    private String nom;

    // Constructeur sans argument obligatoire pour Jackson
    public DepartementDto() {}

    public DepartementDto(Integer id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    // Getters et setters
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
}
