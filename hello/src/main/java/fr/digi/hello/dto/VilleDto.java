package fr.digi.hello.dto;

public class VilleDto {
    private Integer id;
    private String nom;
    private Integer nbHabitants;
    private String departement;  // Nom du département

    public VilleDto() {}

    public VilleDto(Integer id, String nom, Integer nbHabitants, String departement) {
        this.id = id;
        this.nom = nom;
        this.nbHabitants = nbHabitants;
        this.departement = departement;
    }

    // Getters et setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public Integer getNbHabitants() { return nbHabitants; }
    public void setNbHabitants(Integer nbHabitants) { this.nbHabitants = nbHabitants; }
    public String getDepartement() { return departement; }
    public void setDepartement(String departement) { this.departement = departement; }
}
