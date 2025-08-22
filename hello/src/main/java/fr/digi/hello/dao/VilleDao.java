package fr.digi.hello.dao;

import fr.digi.hello.items.Ville;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * DAO (Data Access Object) pour l'entité Ville.
 * Étend JpaRepository pour fournir les opérations CRUD standard.
 * Permet la recherche de Ville par nom, insensible à la casse.
 */
public interface VilleDao extends JpaRepository<Ville, Integer> {

    /**
     * Recherche une ville par son nom (insensible à la casse).
     */
    Optional<Ville> findByNomIgnoreCase(String nom);
    Optional<Ville> findById(Integer id);

}
