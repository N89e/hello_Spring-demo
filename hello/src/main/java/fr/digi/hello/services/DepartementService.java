package fr.digi.hello.services;

import fr.digi.hello.dao.DepartementDao;
import fr.digi.hello.items.Departement;
import fr.digi.hello.items.Ville;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DepartementService {

    private final DepartementDao departementDao;

    public DepartementService(DepartementDao departementDao) {
        this.departementDao = departementDao;
    }

    public List<Departement> extractDepartements() {
        return departementDao.findAll();
    }

    public Optional<Departement> extractDepartement(int id) {
        return departementDao.findById(id);
    }

    public Optional<Departement> extractDepartement(String nom) {
        return departementDao.findByNomIgnoreCase(nom);
    }

    public List<Departement> modifierDepartement(int idDepartement, Departement departementModifie) {
        Optional<Departement> opt = departementDao.findById(idDepartement);
        if (opt.isPresent()) {
            Departement departement = opt.get();
            departement.setNom(departementModifie.getNom());
            departementDao.save(departement);
        }
        return departementDao.findAll();
    }

    public List<Departement> modifierDepartementParNom(String nom, Departement departementModifie) {
        Optional<Departement> opt = departementDao.findByNomIgnoreCase(nom);
        if (opt.isPresent()) {
            Departement dpt = opt.get();
            dpt.setNom(departementModifie.getNom());
            departementDao.save(dpt);
        }
        return departementDao.findAll();
    }


    public List<Departement> supprimerDepartement(int idDepartement) {
        departementDao.deleteById(idDepartement);
        return departementDao.findAll();
    }

    public List<Departement> supprimerDepartementParNom(String nom) {
        Optional<Departement> opt = departementDao.findByNomIgnoreCase(nom);
        opt.ifPresent(department -> departementDao.delete(department));
        return departementDao.findAll();
    }

    public List<Departement> insertDepartement(Departement departement) {
        departementDao.save(departement);
        return departementDao.findAll();
    }
}
