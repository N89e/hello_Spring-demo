package fr.digi.hello.generiqueData;

import fr.digi.hello.items.Departement;
import fr.digi.hello.dao.DepartementDao;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Composant Spring chargé d'insérer les données initiales des départements
 * dans la base de données au démarrage de l'application.
 *
 * Cette classe s'exécute automatiquement grâce à l'implémentation de CommandLineRunner.
 * Elle insère les départements prédéfinis uniquement si la table est vide.
 */
@Component
@Order(1)  // Définit l'ordre d'exécution si plusieurs CommandLineRunner sont présents
public class DepartementDataLoader implements CommandLineRunner {

    private final DepartementDao departementDao;

    /**
     * Constructeur avec injection du DAO de département.
     *
     * @param departementDao DAO pour accéder aux données des départements
     */
    public DepartementDataLoader(DepartementDao departementDao) {
        this.departementDao = departementDao;
    }

    /**
     * Méthode exécutée au démarrage de l'application.
     * Insère les départements prédéfinis si la table est vide.
     *
     * @param args arguments de la ligne de commande (non utilisés)
     * @throws Exception en cas d’erreur d’insertion
     */
    @Override
    public void run(String... args) throws Exception {
        if (departementDao.count() == 0) {
            departementDao.save(new Departement(null, "Gard", "30"));
            departementDao.save(new Departement(null, "Paris", "75"));
            departementDao.save(new Departement(null, "Loire Atlantique", "44"));
            departementDao.save(new Departement(null, "Herault", "34"));
            departementDao.save(new Departement(null, "Tarn","80"));
            departementDao.save(new Departement(null, "Aude","11"));
            departementDao.save(new Departement(null, "Rhône", "69"));
            departementDao.save(new Departement(null, "Bouches-du-Rhône", "13"));
            departementDao.save(new Departement(null, "La Haute-Garonne","31"));
            departementDao.save(new Departement(null, "Alpes-Maritimes","06"));
        }
    }
}
