package fr.digi.hello.services;

import fr.digi.hello.dao.VilleDao;
import fr.digi.hello.items.Ville;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service métier pour la gestion des villes.
 * Fournit des méthodes pour rechercher des villes par identifiant, nom
 * ou selon des critères liés à leur population et à leur département.
 */
@Service
public class ImplVilleService {

    private final VilleDao villeDao;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Constructeur avec injection du DAO.
     *
     * @param villeDao DAO pour accéder aux données des villes
     */
    public ImplVilleService(VilleDao villeDao) {
        this.villeDao = villeDao;
    }

    /**
     * Récupère les {@code n} villes ayant la plus forte population
     * dans un département donné.
     *
     * @param nomDepartement nom du département
     * @param n              nombre maximum de villes à retourner
     * @return liste des villes triées par population décroissante
     */
    public List<Ville> nVillesByDepartementOrderByPopulationDesc(String nomDepartement, int n) {
        String jpql = "SELECT v FROM Ville v WHERE LOWER(v.departement.nom) = LOWER(:nomDepartement) ORDER BY v.nbHabitants DESC";
        TypedQuery<Ville> query = entityManager.createQuery(jpql, Ville.class);
        query.setParameter("nomDepartement", nomDepartement);
        query.setMaxResults(n);
        return query.getResultList();
    }

    /**
     * Récupère les villes d’un département dont la population est comprise
     * entre deux valeurs.
     *
     * @param nomDepartement nom du département
     * @param min            population minimale
     * @param max            population maximale
     * @return liste des villes correspondant aux critères
     */
    public List<Ville> findVillesByDepartementAndPopulationBetween(String nomDepartement, int min, int max) {
        String jpql = "SELECT v FROM Ville v WHERE LOWER(v.departement.nom) = LOWER(:nomDepartement) " +
                "AND v.nbHabitants BETWEEN :min AND :max ORDER BY v.nbHabitants DESC";
        TypedQuery<Ville> query = entityManager.createQuery(jpql, Ville.class);
        query.setParameter("nomDepartement", nomDepartement);
        query.setParameter("min", min);
        query.setParameter("max", max);
        return query.getResultList();
    }

    /**
     * Recherche une ville par son identifiant.
     *
     * @param id identifiant de la ville
     * @return un {@link Optional} contenant la ville si trouvée
     */
    public Optional<Ville> extractVille(Integer id) {
        return villeDao.findById(id);
    }

    /**
     * Recherche une ville par son nom.
     *
     * @param nom nom de la ville
     * @return un {@link Optional} contenant la ville si trouvée
     */
    public Optional<Ville> extractVille(String nom) {
        return villeDao.findByNomIgnoreCase(nom);
    }
}
