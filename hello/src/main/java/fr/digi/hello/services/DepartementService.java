package fr.digi.hello.services;

import fr.digi.hello.dao.DepartementDao;
import fr.digi.hello.items.Departement;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service métier pour la gestion des départements.
 * <p>
 * Fournit des méthodes pour rechercher, insérer, modifier et supprimer
 * des départements via {@link DepartementDao} et l’API {@link EntityManager}.
 */
@Service
@Transactional
public class DepartementService {

    @PersistenceContext
    private EntityManager entityManager;

    private final DepartementDao departementDao;

    /**
     * Constructeur avec injection du DAO.
     *
     * @param departementDao DAO pour accéder aux données des départements
     */
    public DepartementService(DepartementDao departementDao) {
        this.departementDao = departementDao;
    }

    /**
     * Recherche un département par son code.
     *
     * @param code code du département
     * @return un {@link Optional} contenant le département s’il existe
     */
    public Optional<Departement> findByCode(String code) {
        return departementDao.findByCodeIgnoreCase(code);
    }

    /**
     * Récupère l’ensemble des départements.
     *
     * @return liste des départements
     */
    public List<Departement> extractDepartements() {
        return entityManager.createQuery("SELECT d FROM Departement d", Departement.class)
                .getResultList();
    }

    /**
     * Recherche un département par son identifiant.
     *
     * @param id identifiant du département
     * @return un {@link Optional} contenant le département s’il existe
     */
    public Optional<Departement> extractDepartement(int id) {
        return departementDao.findById(id);
    }

    /**
     * Recherche un département par son nom (insensible à la casse).
     *
     * @param nom nom du département
     * @return un {@link Optional} contenant le département s’il existe
     */
    public Optional<Departement> extractDepartement(String nom) {
        List<Departement> departements = entityManager.createQuery(
                        "SELECT d FROM Departement d WHERE LOWER(d.nom) = LOWER(:nom)", Departement.class)
                .setParameter("nom", nom)
                .getResultList();

        return departements.stream().findFirst();
    }

    /**
     * Insère un nouveau département.
     *
     * @param departement département à persister
     * @return liste mise à jour des départements
     */
    public List<Departement> insertDepartement(Departement departement) {
        departementDao.save(departement);
        return departementDao.findAll();
    }

    /**
     * Met à jour un département en fonction de son identifiant.
     *
     * @param idDepartement identifiant du département à modifier
     * @param departementModifie nouvelles données
     * @return liste mise à jour des départements
     */
    public List<Departement> modifierDepartement(int idDepartement, Departement departementModifie) {
        Optional<Departement> opt = departementDao.findById(idDepartement);
        if (opt.isPresent()) {
            Departement departement = opt.get();
            departement.setNom(departementModifie.getNom());
            departementDao.save(departement);
        }
        return departementDao.findAll();
    }

    /**
     * Met à jour un département en fonction de son nom.
     *
     * @param nom nom du département actuel
     * @param departementModifie nouvelles données
     * @return liste mise à jour des départements
     */
    public List<Departement> modifierDepartementParNom(String nom, Departement departementModifie) {
        List<Departement> departements = entityManager.createQuery(
                        "SELECT d FROM Departement d WHERE LOWER(d.nom) = LOWER(:nom)", Departement.class)
                .setParameter("nom", nom)
                .getResultList();

        if (!departements.isEmpty()) {
            Departement departement = departements.get(0);
            departement.setNom(departementModifie.getNom());
            departement.setCode(departementModifie.getCode());
            entityManager.merge(departement);
        }

        return entityManager.createQuery("SELECT d FROM Departement d", Departement.class)
                .getResultList();
    }

    /**
     * Supprime un département par identifiant.
     *
     * @param idDepartement identifiant du département à supprimer
     * @return liste mise à jour des départements
     */
    public List<Departement> supprimerDepartement(int idDepartement) {
        departementDao.deleteById(idDepartement);
        return departementDao.findAll();
    }

    /**
     * Supprime un département par son nom.
     *
     * @param nom nom du département à supprimer
     * @return liste mise à jour des départements
     */
    public List<Departement> supprimerDepartementParNom(String nom) {
        Optional<Departement> opt = departementDao.findByNomIgnoreCase(nom);
        if (opt.isPresent()) {
            Departement departement = opt.get();

            if (entityManager.contains(departement)) {
                entityManager.remove(departement);
            } else {
                entityManager.remove(entityManager.merge(departement));
            }
        }
        return entityManager.createQuery("SELECT d FROM Departement d", Departement.class)
                .getResultList();
    }
}
