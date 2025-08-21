package fr.digi.hello.items;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

/**
 * Représente une ville avec un identifiant, un nom et un nombre d'habitants.
 * Cette classe utilise des annotations Bean Validation pour valider ses attributs.
 */
@Entity
@Table(name = "villes")
public class Ville {

    /**
     * Identifiant unique de la ville.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Nom de la ville. Ne peut pas être nul et doit contenir au moins 2 caractères.
     */
    private String nom;

    /**
     * Nombre d'habitants de la ville. Ne peut pas être nul et doit être supérieur ou égal à 1.
     */
    private Integer nbHabitants;

    @ManyToOne
    @JoinColumn(name = "departement_id")
    @JsonIgnoreProperties("villes")
    private Departement departement;

    /**
     * Constructeur vide par défaut.
     */
    public Ville() {}

    /**
     * Constructeur avec paramètres.
     *
     * @param id Identifiant unique de la ville.
     * @param nom Nom de la ville.
     * @param nbHabitants Nombre d'habitants.
     */
    public Ville(Integer id, String nom, Integer nbHabitants, Departement departement) {
        this.id = id;
        this.nom = nom;
        this.nbHabitants = nbHabitants;
        this.departement = departement;
    }

    /**
     * Récupère l'identifiant de la ville.
     *
     * @return l'id de la ville.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Définit l'identifiant de la ville.
     *
     * @param id Nouvel identifiant à attribuer.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Récupère le nom de la ville.
     *
     * @return le nom de la ville.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Définit le nom de la ville.
     *
     * @param nom Nouveau nom à attribuer.
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Récupère le nombre d'habitants de la ville.
     *
     * @return le nombre d'habitants.
     */
    public Integer getNbHabitants() {
        return nbHabitants;
    }

    /**
     * Définit le nombre d'habitants de la ville.
     *
     * @param nbHabitants Nouveau nombre d'habitants à attribuer.
     */
    public void setNbHabitants(int nbHabitants) {

        this.nbHabitants = nbHabitants;
    }

    public Departement getDepartement() {
        return departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }
}
