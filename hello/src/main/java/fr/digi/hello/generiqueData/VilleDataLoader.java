package fr.digi.hello.generiqueData;

import fr.digi.hello.dao.DepartementDao;
import fr.digi.hello.items.Departement;
import fr.digi.hello.items.Ville;
import fr.digi.hello.dao.VilleDao;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Chargement initial des données pour les villes.
 * Cette classe insère dans la base des villes avec leur département associé, uniquement si la table est vide.
 * Elle est exécutée automatiquement au démarrage de l'application.
 */
@Component
@Order(2)  // Ordre d’exécution si plusieurs loaders
public class VilleDataLoader implements CommandLineRunner {

    private final VilleDao villeDao;
    private final DepartementDao departementDao;

    /**
     * Constructeur avec injection des DAO nécessaires.
     *
     * @param villeDao       DAO pour l'entité Ville
     * @param departementDao DAO pour l'entité Departement
     */
    public VilleDataLoader(VilleDao villeDao, DepartementDao departementDao) {
        this.villeDao = villeDao;
        this.departementDao = departementDao;
    }

    /**
     * Recherche un département par son nom (insensible à la casse).
     * Lance une exception si le département n'est pas trouvé.
     *
     * @param nom nom du département recherché
     * @return le département correspondant
     * @throws RuntimeException si aucun département ne correspond au nom donné
     */
    private Departement getDepartementByName(String nom) {
        return departementDao.findByNomIgnoreCase(nom)
                .orElseThrow(() -> new RuntimeException("Département '" + nom + "' non trouvé"));
    }

    /**
     * Méthode appelée automatiquement au démarrage de l’application.
     * Charge des villes prédéfinies associées à leurs départements si la table des villes est vide.
     *
     * @param args arguments de la ligne de commande (non utilisés)
     * @throws Exception en cas d’erreur lors de l’insertion
     */
    @Override
    public void run(String... args) throws Exception {
        if (villeDao.count() == 0) {
            villeDao.save(new Ville(null, "Nîmes", 150000, getDepartementByName("Gard")));
            villeDao.save(new Ville(null, "Lyon", 515695, getDepartementByName("Rhône")));
            villeDao.save(new Ville(null, "Marseille", 861635, getDepartementByName("Bouches-du-Rhône")));
            villeDao.save(new Ville(null, "Toulouse", 479553, getDepartementByName("La Haute-Garonne")));
            villeDao.save(new Ville(null, "Nice", 342669, getDepartementByName("Alpes-Maritimes")));
        }
    }
}
