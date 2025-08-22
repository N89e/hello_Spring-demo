package fr.digi.hello.dto;

/**
 * Data Transfer Object (DTO) représentant un département.
 * Utilisé pour transférer les données d’un département entre différentes couches de l'application.
 */
public class DepartementDto {

    /** Identifiant unique du département (facultatif selon usage) */
    private Integer id;

    /** Nom du département */
    private String nom;
    /** Code du département */
    private String code;

    /**
     * Constructeur par défaut requis par Jackson pour la sérialisation/désérialisation JSON.
     */
    public DepartementDto() {}

    /**
     * Constructeur complet avec initialisation des attributs.
     *
     * @param id identifiant unique du département
     * @param nom nom du département
     */
    public DepartementDto(Integer id, String nom, String code) {
        this.id = id;
        this.nom = nom;
        this.code = code;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
