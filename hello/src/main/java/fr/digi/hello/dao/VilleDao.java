package fr.digi.hello.dao;

import fr.digi.hello.items.Ville;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VilleDao extends JpaRepository<Ville, Integer> {
    Optional<Ville> findByNomIgnoreCase(String nom);
}

