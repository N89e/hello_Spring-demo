package fr.digi.hello.services;

import fr.digi.hello.dao.VilleDao;
import fr.digi.hello.items.Ville;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VilleService {

    private final VilleDao villeDao;

    public VilleService(VilleDao villeDao) {
        this.villeDao = villeDao;
    }

    public List<Ville> extractVilles() {
        return villeDao.findAll();
    }

    public Optional<Ville> extractVille(String nom) {
        return villeDao.findByNomIgnoreCase(nom);
    }

    /**
     * Insère une nouvelle ville.
     */
    public List<Ville> insertVille(Ville ville) {
        villeDao.save(ville);
        return villeDao.findAll();
    }

    /**
     * Modifie une ville identifiée par son nom.
     */
    public List<Ville> modifierVilleParNom(String nom, Ville villeModifiee) {
        Optional<Ville> opt = villeDao.findByNomIgnoreCase(nom);
        if (opt.isPresent()) {
            Ville ville = opt.get();
            ville.setNom(villeModifiee.getNom());
            ville.setNbHabitants(villeModifiee.getNbHabitants());
            villeDao.save(ville);
        }
        return villeDao.findAll();
    }

    /**
     * Supprime une ville identifiée par son nom.
     */
    public List<Ville> supprimerVilleParNom(String nom) {
        Optional<Ville> opt = villeDao.findByNomIgnoreCase(nom);
        opt.ifPresent(ville -> villeDao.delete(ville));
        return villeDao.findAll();
    }

}
