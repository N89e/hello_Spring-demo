package fr.digi.hello.validators;

import fr.digi.hello.dto.DepartementDto;
import fr.digi.hello.dto.VilleDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validateur personnalisé pour les {@link VilleDto}.
 * <p>
 * Vérifie la validité des propriétés d’une ville avant traitement
 * métier ou persistance.
 */
@Component
public class VilleValidator implements Validator {

    private final DepartementValidator departementValidator;

    /**
     * Constructeur avec injection du validateur de département.
     *
     * @param departementValidator validateur utilisé pour vérifier le département associé
     */
    public VilleValidator(DepartementValidator departementValidator) {
        this.departementValidator = departementValidator;
    }

    /**
     * Indique si ce validateur peut s’appliquer à la classe donnée.
     *
     * @param clazz type de l’objet à valider
     * @return {@code true} si la classe est un {@link VilleDto}, sinon {@code false}
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return VilleDto.class.isAssignableFrom(clazz);
    }

    /**
     * Effectue la validation d’un {@link VilleDto}.
     *
     * @param target objet à valider (doit être de type {@link VilleDto})
     * @param errors collecteur des erreurs de validation détectées
     */
    @Override
    public void validate(Object target, Errors errors) {
        VilleDto ville = (VilleDto) target;

        if (ville.getNom() == null || ville.getNom().trim().isEmpty()) {
            errors.rejectValue("nom", "NomVide", "Le nom ne peut pas être vide");
        } else {
            String nom = ville.getNom().trim();
            if (nom.matches(".*\\d.*")) {
                errors.rejectValue("nom", "FormatNom", "Le nom ne doit pas contenir de chiffres");
            } else if (nom.startsWith("-")) {
                errors.rejectValue("nom", "FormatNom", "Le nom ne doit pas commencer par un tiret");
            } else if (nom.length() < 2) {
                errors.rejectValue("nom", "NomCourt", "Le nom doit contenir au moins 2 caractères");
            }
        }

        if (ville.getNbHabitants() == null) {
            errors.rejectValue("nbHabitants", "NbHabitantsVide", "Le nombre d'habitants est obligatoire");
        } else if (ville.getNbHabitants() <= 0) {
            errors.rejectValue("nbHabitants", "NbHabitantsInvalide", "Le nombre d'habitants doit être supérieur ou égal à 1");
        }

        if (ville.getNomDepartement() != null || ville.getCodeDpt() != null) {
            DepartementDto departementDto = new DepartementDto();
            departementDto.setNom(ville.getNomDepartement());
            departementDto.setCode(ville.getCodeDpt());

            departementValidator.validate(departementDto, errors);
        }
    }
}
