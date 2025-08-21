package fr.digi.hello.mappers;

import fr.digi.hello.dto.DepartementDto;
import fr.digi.hello.dto.VilleDto;
import fr.digi.hello.items.Departement;
import fr.digi.hello.items.Ville;

public class MapperUtil {


    public static VilleDto toVilleDto(Ville ville) {
        if (ville == null) return null;

        String nomDepartement = null;
        if (ville.getDepartement() != null) {
            nomDepartement = ville.getDepartement().getNom();
        }

        return new VilleDto(
                ville.getId(),
                ville.getNom(),
                ville.getNbHabitants(),
                nomDepartement
        );
    }

    // Mapping VilleDto + Departement -> Ville
    public static Ville toVille(VilleDto villeDto, Departement departement) {
        if (villeDto == null) return null;
        return new Ville(
                villeDto.getId(),
                villeDto.getNom(),
                villeDto.getNbHabitants(),
                departement
        );
    }

    // Mapping Departement -> DepartementDto
    public static DepartementDto toDepartementDto(Departement departement) {
        if (departement == null) return null;

        return new DepartementDto(
                departement.getId(),
                departement.getNom()
        );
    }

    // Mapping DepartementDto -> Departement
    public static Departement toDepartement(DepartementDto departementDto) {
        if (departementDto == null) return null;

        Departement departement = new Departement();
        departement.setId(departementDto.getId());
        departement.setNom(departementDto.getNom());

        return departement;
    }
}
