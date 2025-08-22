package fr.digi.hello.validators;

import fr.digi.hello.dto.DepartementDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validateur personnalisé pour les {@link DepartementDto}.
 * <p>
 * Vérifie que le code et le nom d’un département sont valides.
 */
@Component
public class DepartementValidator implements Validator {

    /**
     * Indique si ce validateur peut s’appliquer à la classe donnée.
     *
     * @param clazz type de l’objet à valider
     * @return {@code true} si la classe est un {@link DepartementDto}, {@code false} sinon
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return DepartementDto.class.isAssignableFrom(clazz);
    }

    /**
     * Valide un {@link DepartementDto}.
     *
     * @param target objet à valider (doit être de type {@link DepartementDto})
     * @param errors objet collectant les erreurs de validation
     */
    @Override
    public void validate(Object target, Errors errors) {
        DepartementDto departementDto = (DepartementDto) target;

        // Validation du code département
        if (departementDto.getCode() == null || departementDto.getCode().trim().isEmpty()) {
            errors.rejectValue("code", "CodeDptVide", "Le code du département ne peut pas être vide");
        }

        // Validation du nom de département
        if (departementDto.getNom() == null || departementDto.getNom().trim().isEmpty()) {
            errors.rejectValue("nom", "NomVide", "Le nom du département ne peut pas être vide");
        } else {
            String nomDept = departementDto.getNom().trim();

            if (nomDept.matches(".*\\d.*")) {
                errors.rejectValue("nom", "FormatNomDepartement", "Le nom du département ne doit pas contenir de chiffres");
            } else if (nomDept.startsWith("-")) {
                errors.rejectValue("nom", "FormatNomDepartement", "Le nom du département ne doit pas commencer par un tiret");
            } else if (nomDept.length() < 2) {
                errors.rejectValue("nom", "NomDepartementCourt", "Le nom du département doit contenir au moins 2 caractères");
            }
        }
    }
}
