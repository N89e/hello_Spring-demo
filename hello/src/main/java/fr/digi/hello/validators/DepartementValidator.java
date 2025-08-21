package fr.digi.hello.validators;

import fr.digi.hello.dto.DepartementDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class DepartementValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return DepartementDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        DepartementDto departement = (DepartementDto) target;

        if (departement.getNom() == null || departement.getNom().trim().isEmpty()) {
            errors.rejectValue("nom", "NomVide", "Le nom du département ne peut pas être vide");
            return;
        }
        if (departement.getNom().trim().length() < 2) {
            errors.rejectValue("nom", "NomCourt", "Le nom doit contenir au moins 2 caractères");
        }
    }
}
