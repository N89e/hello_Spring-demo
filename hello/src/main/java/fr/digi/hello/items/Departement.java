package fr.digi.hello.items;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité JPA représentant un département.
 * Un département possède un identifiant unique (id), un code officiel et un nom.
 * Il est relié à une liste de villes ({@link Ville}).
 */
@Entity
@Table(name = "departement")
public class Departement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Nom du département. */
    private String nom;

    /** Code officiel du département (ex: "33"). */
    private String code;

    /**
     * Liste des villes rattachées à ce département.
     * Relation {@code OneToMany} avec {@link Ville}.
     * Le cascade ALL permet de propager les opérations.
     * {@code @JsonIgnoreProperties("departement")} évite la récursivité infinie
     * lors de la sérialisation JSON.
     */
    @OneToMany(mappedBy = "departement", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("departement")
    private List<Ville> villes;

    /** Initialisation automatique de la liste pour éviter les NullPointerException. */
    {
        villes = new ArrayList<>();
    }

    /** Constructeur par défaut requis par JPA. */
    public Departement(){
    }

    /**
     * Constructeur pratique pour initialiser l’entité.
     *
     * @param id identifiant du département
     * @param nom nom du département
     * @param code code officiel du département
     */
    public Departement(Integer id, String nom, String code){
        this.id = id;
        this.nom = nom;
        this.code = code;
    }

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

    public List<Ville> getVilles() {
        return villes;
    }

    public void setVilles(List<Ville> villes) {
        this.villes = villes;
    }
}
