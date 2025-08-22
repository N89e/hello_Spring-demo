package fr.digi.hello.dao;

import fr.digi.hello.items.Departement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartementDao extends JpaRepository<Departement, Integer> {

    Optional<Departement> findByNomIgnoreCase(String nom);
    Optional<Departement> findByCodeIgnoreCase(String code);

}
