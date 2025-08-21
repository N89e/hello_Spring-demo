package fr.digi.hello.services;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Représente une ville avec un identifiant, un nom et un nombre d'habitants.
 * Cette classe utilise des annotations Bean Validation pour valider ses attributs.
 */
public class Ville {

    /**
     * Identifiant unique de la ville.
     */
    private int id;

    /**
     * Nom de la ville. Ne peut pas être nul et doit contenir au moins 2 caractères.
     */
//    @NotNull(message = "Le nom de la ville ne peut pas être nul")
//    @Size(min = 2, message = "Le nom de la ville doit contenir au moins 2 caractères")
    private String nom;

    /**
     * Nombre d'habitants de la ville. Ne peut pas être nul et doit être supérieur ou égal à 1.
     */
//    @NotNull(message = "Le nombre d'habitant de la ville ne peut pas être nul")
//    @Min(value = 1, message = "Le nombre d'habitants doit être supérieur ou égal à 1")
    private Integer nbHabitants;

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
    public Ville(int id, String nom, Integer nbHabitants) {
        this.id = id;
        this.nom = nom;
        this.nbHabitants = nbHabitants;
    }

    /**
     * Récupère l'identifiant de la ville.
     *
     * @return l'id de la ville.
     */
    public int getId() {
        return id;
    }

    /**
     * Définit l'identifiant de la ville.
     *
     * @param id Nouvel identifiant à attribuer.
     */
    public void setId(int id) {
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
}
