package fr.digi.hello.validators;

import fr.digi.hello.dto.VilleDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class VilleValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return VilleDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        VilleDto ville = (VilleDto) target;

        // Validation du nom : non null et au moins 2 caractères
        if (ville.getNom() == null || ville.getNom().trim().isEmpty()) {
            errors.rejectValue("nom", "NomVide", "Le nom ne peut pas être vide");
        } else if (ville.getNom().length() < 2) {
            errors.rejectValue("nom", "NomCourt", "Le nom doit contenir au moins 2 caractères");
        }

        // Validation du nombre d'habitants : non null et >= 1
        if (ville.getNbHabitants() == null) {
            errors.rejectValue("nbHabitants", "NbHabitantsVide", "Le nombre d'habitants est obligatoire");
        } else if (ville.getNbHabitants() < 1) {
            errors.rejectValue("nbHabitants", "NbHabitantsInvalide", "Le nombre d'habitants doit être supérieur ou égal à 1");
        }

        // Validation du nom du département (optionnelle, mais si présent non vide)
        if (ville.getDepartement() != null && ville.getDepartement().trim().isEmpty()) {
            errors.rejectValue("departement", "DepartementVide", "Le nom du département ne peut pas être vide");
        }
    }
}
