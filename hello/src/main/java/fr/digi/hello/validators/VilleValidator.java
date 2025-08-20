package fr.digi.hello.validators;

import fr.digi.hello.services.Ville;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validator personnalisé pour valider les contraintes métier de l'objet Ville.
 * Vérifie que le nom est non nul et d'au moins 2 caractères,
 * et que le nombre d'habitants est supérieur ou égal à 1.
 */
@Component
public class VilleValidator implements Validator {

    /**
     * Indique que ce validator supporte la classe Ville.
     *
     * @param clazz La classe à vérifier.
     * @return true si la classe est assignable de Ville, false sinon.
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return Ville.class.isAssignableFrom(clazz);
    }

    /**
     * Valide les attributs de l'objet Ville.
     *
     * @param target L'objet Ville à valider.
     * @param errors L'objet Errors pour enregistrer les erreurs éventuelles.
     */
    @Override
    public void validate(Object target, Errors errors) {
        Ville ville = (Ville) target;

        if (ville.getNom() == null || ville.getNom().length() < 2) {
            errors.rejectValue("nom", "nom.size", "Le nom de la ville doit contenir au moins 2 caractères");
        }
        if (ville.getNbHabitants() < 1) {
            errors.rejectValue("nbHabitants", "habitants.min", "Le nombre d'habitants doit être supérieur ou égal à 1");
        }
    }
}
