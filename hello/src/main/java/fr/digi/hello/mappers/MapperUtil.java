package fr.digi.hello.mappers;

import fr.digi.hello.dto.DepartementDto;
import fr.digi.hello.dto.VilleDto;
import fr.digi.hello.items.Departement;
import fr.digi.hello.items.Ville;
import org.springframework.stereotype.Component;

/**
 * Utilitaire central de conversion entre entités JPA ({@link Ville}, {@link Departement})
 * et leurs objets de transfert correspondants ({@link VilleDto}, {@link DepartementDto}).
 * <p>
 * Permet d’isoler la logique de mapping et de faciliter la communication
 * entre la couche persistance et la couche présentation.
 */
@Component
public class MapperUtil {

    /**
     * Transforme une entité {@link Ville} en un {@link VilleDto}.
     *
     * @param ville entité Ville à convertir (peut être {@code null})
     * @return DTO correspondant, ou {@code null} si l’entrée est {@code null}
     */
    public static VilleDto toVilleDto(Ville ville) {
        if (ville == null) return null;
        String nomDepartement = null;
        String codeDepartement = null;

        if (ville.getDepartement() != null) {
            nomDepartement = ville.getDepartement().getNom();
            codeDepartement = ville.getDepartement().getCode();
        }

        return new VilleDto(
                ville.getId(),
                ville.getNom(),
                ville.getNbHabitants(),
                nomDepartement,
                codeDepartement
        );
    }

    /**
     * Transforme un {@link VilleDto} en entité {@link Ville}.
     *
     * @param villeDto    DTO de la ville à convertir (peut être {@code null})
     * @param departement entité {@link Departement} associée,
     *                    permet de relier la ville à son département
     * @return entité Ville, ou {@code null} si le DTO est {@code null}
     */
    public static Ville toVille(VilleDto villeDto, Departement departement) {
        if (villeDto == null) return null;
        return new Ville(
                villeDto.getId(),
                villeDto.getNom(),
                villeDto.getNbHabitants(),
                departement
        );
    }

    /**
     * Transforme une entité {@link Departement} en {@link DepartementDto}.
     *
     * @param departement entité à convertir (peut être {@code null})
     * @return DTO correspondant, ou {@code null} si l’entrée est {@code null}
     */
    public static DepartementDto toDepartementDto(Departement departement) {
        if (departement == null) return null;
        return new DepartementDto(
                departement.getId(),
                departement.getNom(),
                departement.getCode()
        );
    }

    /**
     * Transforme un {@link DepartementDto} en entité {@link Departement}.
     *
     * @param departementDto DTO à convertir (peut être {@code null})
     * @return entité correspondante, ou {@code null} si le DTO est {@code null}
     */
    public static Departement toDepartement(DepartementDto departementDto) {
        if (departementDto == null) return null;
        Departement departement = new Departement();
        departement.setId(departementDto.getId());
        departement.setNom(departementDto.getNom());
        departement.setCode(departementDto.getCode());
        return departement;
    }
}
