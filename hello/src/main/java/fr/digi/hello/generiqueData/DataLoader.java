package fr.digi.hello.generiqueData;

import fr.digi.hello.items.Ville;
import fr.digi.hello.dao.VilleDao;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final VilleDao villeDao;

    public DataLoader(VilleDao villeDao) {
        this.villeDao = villeDao;
    }

    @Override
    public void run(String... args) throws Exception {
        // Vérifie si la table est vide avant d’insérer
        if (villeDao.count() == 0) {
            villeDao.save(new Ville(null, "Paris", 2148000));
            villeDao.save(new Ville(null, "Lyon", 515695));
            villeDao.save(new Ville(null, "Marseille", 861635));
            villeDao.save(new Ville(null, "Toulouse", 479553));
            villeDao.save(new Ville(null, "Nice", 342669));
        }
    }
}
