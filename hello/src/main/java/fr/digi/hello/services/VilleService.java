package fr.digi.hello.services;

import fr.digi.hello.dao.VilleDao;
import fr.digi.hello.items.Ville;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service métier pour la gestion des villes.
 * <p>
 * Fournit des opérations CRUD et des méthodes de recherche
 * par identifiant ou par nom.
 */
@Service
@Transactional
public class VilleService {

    private final VilleDao villeDao;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Constructeur avec injection du DAO.
     *
     * @param villeDao DAO permettant l'accès aux données des villes
     */
    public VilleService(VilleDao villeDao) {
        this.villeDao = villeDao;
    }

    /**
     * Récupère toutes les villes.
     *
     * @return liste des villes
     */
    public List<Ville> extractVilles() {
        return entityManager.createQuery("SELECT v FROM Ville v", Ville.class)
                .getResultList();
    }

    /**
     * Recherche une ville par identifiant.
     *
     * @param id identifiant de la ville
     * @return un {@link Optional} contenant la ville si trouvée
     */
    public Optional<Ville> extractVille(Integer id) {
        return villeDao.findById(id);
    }

    /**
     * Recherche une ville par nom (insensible à la casse).
     *
     * @param nom nom de la ville
     * @return un {@link Optional} contenant la ville si trouvée
     */
    public Optional<Ville> extractVille(String nom) {
        List<Ville> villes = entityManager.createQuery(
                        "SELECT v FROM Ville v WHERE LOWER(v.nom) = LOWER(:nom)", Ville.class)
                .setParameter("nom", nom)
                .getResultList();

        return villes.stream().findFirst();
    }

    /**
     * Insère une nouvelle ville.
     *
     * @param ville ville à sauvegarder
     * @return liste mise à jour des villes
     */
    public List<Ville> insertVille(Ville ville) {
        villeDao.save(ville);
        return villeDao.findAll();
    }

    /**
     * Met à jour une ville existante en fonction de son identifiant.
     *
     * @param idVille identifiant de la ville à modifier
     * @param villeModifie données modifiées
     * @return liste mise à jour des villes
     */
    public List<Ville> modifierVille(Integer idVille, Ville villeModifie) {
        Optional<Ville> opt = villeDao.findById(idVille);
        if (opt.isPresent()) {
            Ville ville = opt.get();
            ville.setNom(villeModifie.getNom().toLowerCase());
            ville.setNbHabitants(villeModifie.getNbHabitants());

            // Mise à jour du département si différent et non nul
            if (villeModifie.getDepartement() != null) {
                ville.setDepartement(villeModifie.getDepartement());
            }

            villeDao.save(ville);
        }
        return villeDao.findAll();
    }

    /**
     * Met à jour une ville existante en fonction de son nom.
     *
     * @param nom nom actuel de la ville
     * @param villeModifiee données modifiées
     * @return liste mise à jour des villes
     */
    public List<Ville> modifierVilleParNom(String nom, Ville villeModifiee) {
        List<Ville> villes = entityManager.createQuery(
                        "SELECT v FROM Ville v WHERE LOWER(v.nom) = LOWER(:nom)", Ville.class)
                .setParameter("nom", nom)
                .getResultList();

        if (!villes.isEmpty()) {
            Ville ville = villes.get(0);

            if (villeModifiee.getNom() != null && !villeModifiee.getNom().isBlank()) {
                ville.setNom(villeModifiee.getNom());
            }
            if (villeModifiee.getNbHabitants() != null && villeModifiee.getNbHabitants() > 0) {
                ville.setNbHabitants(villeModifiee.getNbHabitants());
            }
            if (villeModifiee.getDepartement() != null) {
                ville.setDepartement(villeModifiee.getDepartement());
            }

            entityManager.merge(ville);
        }

        return entityManager.createQuery("SELECT v FROM Ville v", Ville.class)
                .getResultList();
    }


    /**
     * Supprime une ville par identifiant.
     *
     * @param idVille identifiant de la ville
     * @return liste mise à jour des villes
     */
    public List<Ville> supprimerVille(Integer idVille) {
        villeDao.deleteById(idVille);
        return villeDao.findAll();
    }

    /**
     * Supprime une ville par nom.
     *
     * @param nom nom de la ville à supprimer
     * @return liste mise à jour des villes
     */
    public List<Ville> supprimerVilleParNom(String nom) {
        Optional<Ville> opt = villeDao.findByNomIgnoreCase(nom);
        if (opt.isPresent()) {
            List<Ville> villes = entityManager.createQuery(
                            "SELECT v FROM Ville v WHERE LOWER(v.nom) = LOWER(:nom)", Ville.class)
                    .setParameter("nom", nom)
                    .getResultList();
            Ville ville = villes.get(0);
            entityManager.remove(ville);
        }

        return entityManager.createQuery("SELECT v FROM Ville v", Ville.class)
                .getResultList();
    }
}
