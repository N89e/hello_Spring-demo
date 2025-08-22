package fr.digi.hello.dto;

/**
 * Data Transfer Object (DTO) représentant une ville.
 * Utilisé pour transférer les données liées à une ville entre différentes couches de l'application.
 */
public class VilleDto {

    private Integer id;
    private String nom;
    private Integer nbHabitants;
    private String nomDepartement;
    private String codeDpt;

    public VilleDto() {}

    /**
     * Constructeur complet
     * @param id identifiant unique de la ville
     * @param nom nom de la ville
     * @param nbHabitants nombre d'habitants
     * @param nomDepartement nom du département
     */
    public VilleDto(Integer id, String nom, Integer nbHabitants, String nomDepartement, String codeDpt) {
        this.id = id;
        this.nom = nom;
        this.nbHabitants = nbHabitants;
        this.nomDepartement = nomDepartement;
        this.codeDpt = codeDpt;
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

    public Integer getNbHabitants() {
        return nbHabitants;
    }

    public void setNbHabitants(Integer nbHabitants) {
        this.nbHabitants = nbHabitants;
    }

    public String getNomDepartement() {
        return nomDepartement;
    }

    public void setNomDepartement(String nomDepartement) {
        this.nomDepartement = nomDepartement;
    }

    public String getCodeDpt() {
        return codeDpt;
    }

    public void setCodeDpt(String codeDpt) {
        this.codeDpt = codeDpt;
    }
}
