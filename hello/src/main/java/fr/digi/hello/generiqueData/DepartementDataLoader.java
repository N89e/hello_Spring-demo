package fr.digi.hello.generiqueData;

import fr.digi.hello.items.Departement;
import fr.digi.hello.dao.DepartementDao;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class DepartementDataLoader implements CommandLineRunner {

    private final DepartementDao departementDao;

    public DepartementDataLoader(DepartementDao departementDao) {
        this.departementDao = departementDao;
    }

    @Override
    public void run(String... args) throws Exception {
        if(departementDao.count() == 0) {
            departementDao.save(new Departement(null, "Gard"));
            departementDao.save(new Departement(null, "Île de France"));
            departementDao.save(new Departement(null, "Loire Atlantique"));
            departementDao.save(new Departement(null, "Herault"));
            departementDao.save(new Departement(null, "Tarn"));
            departementDao.save(new Departement(null, "Aude"));
            departementDao.save(new Departement(null, "Rhône"));
            departementDao.save(new Departement(null, "Bouches-du-Rhône"));
            departementDao.save(new Departement(null, "La Haute-Garonne"));
            departementDao.save(new Departement(null, "Alpes-Maritimes"));

        }
    }
}


