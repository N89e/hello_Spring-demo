package fr.digi.hello.items;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

/**
 * Entité JPA représentant une ville.
 * Une ville possède un identifiant unique, un nom et un nombre d’habitants.
 * Elle est reliée à un {@link Departement} via une relation {@code ManyToOne}.
 */
@Entity
@Table(name = "villes")
public class Ville {

    /** Identifiant unique de la ville (auto-généré). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Nom de la ville. */
    private String nom;

    /** Nombre d’habitants de la ville. */
    private Integer nbHabitants;

    /**
     * Département auquel appartient la ville.
     * Relation {@code ManyToOne} : plusieurs villes peuvent être liées à un département.
     * L’annotation {@code @JsonIgnoreProperties("villes")} évite une boucle infinie
     * lors de la sérialisation JSON.
     */
    @ManyToOne
    @JoinColumn(name = "departement_id")
    @JsonIgnoreProperties("villes")
    private Departement departement;

    /** Constructeur par défaut requis par JPA. */
    public Ville() {
    }

    /**
     * Constructeur complet.
     *
     * @param id identifiant unique
     * @param nom nom de la ville
     * @param nbHabitants nombre d’habitants
     * @param departement département associé
     */
    public Ville(Integer id, String nom, Integer nbHabitants, Departement departement) {
        this.id = id;
        this.nom = nom;
        this.nbHabitants = nbHabitants;
        this.departement = departement;
    }

    /**
     * Met à jour le nombre d’habitants de la ville.
     *
     * @param nbHabitants nouveau nombre d’habitants
     */
    public void setNbHabitants(int nbHabitants) {
        this.nbHabitants = nbHabitants;
    }

    // Getters / Setters classiques (pas besoin de JavaDoc détaillée)

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

    public Departement getDepartement() {
        return departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }
}
