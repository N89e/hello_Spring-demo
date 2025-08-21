package fr.digi.hello.generiqueData;

import fr.digi.hello.dao.DepartementDao;
import fr.digi.hello.items.Departement;
import fr.digi.hello.items.Ville;
import fr.digi.hello.dao.VilleDao;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class VilleDataLoader implements CommandLineRunner {

    private final VilleDao villeDao;
    private final DepartementDao departementDao;

    public VilleDataLoader(VilleDao villeDao, DepartementDao departementDao) {
        this.villeDao = villeDao;
        this.departementDao = departementDao;
    }

    private Departement getDepartementByName(String nom) {
        return departementDao.findByNomIgnoreCase(nom)
                .orElseThrow(() -> new RuntimeException("Département '" + nom + "' non trouvé"));
    }

    @Override
    public void run(String... args) throws Exception {
        if(villeDao.count() == 0) {
            villeDao.save(new Ville(null, "Nîmes", 150000, getDepartementByName("Gard")));
            villeDao.save(new Ville(null, "Lyon", 515695, getDepartementByName("Rhône")));
            villeDao.save(new Ville(null, "Marseille", 861635, getDepartementByName("Bouches-du-Rhône")));
            villeDao.save(new Ville(null, "Toulouse", 479553, getDepartementByName("La Haute-Garonne")));
            villeDao.save(new Ville(null, "Nice", 342669, getDepartementByName("Alpes-Maritimes")));
        }
    }
}

