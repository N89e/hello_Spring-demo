package fr.digi.hello.validators;

import fr.digi.hello.services.Ville;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class VilleValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Ville.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Ville ville = (Ville) target;

        if (ville.getNom() == null || ville.getNom().length() < 2) {
            errors.rejectValue("nom", "nom.size", "Le nom de la ville doit contenir au moins 2 caractères");
        }
        if (ville.getNbHabitants() < 1) {
            errors.rejectValue("habitants", "habitants.min", "Le nombre d'habitants doit être supérieur ou égal à 1");
        }
    }
}
