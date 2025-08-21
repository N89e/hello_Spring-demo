package fr.digi.hello.controleurs;

import fr.digi.hello.items.Ville;
import fr.digi.hello.services.VilleService;
import fr.digi.hello.validators.VilleValidator;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour gérer les opérations CRUD sur les villes.
 * Utilise le nom de la ville pour identifier chaque ressource.
 */
@RestController
@RequestMapping("/villes")
public class VilleControleur {

    private final VilleService villeService;
    private final VilleValidator villeValidator;

    public VilleControleur(VilleService villeService, VilleValidator villeValidator) {
        this.villeService = villeService;
        this.villeValidator = villeValidator;
    }

    /**
     * Récupère la liste de toutes les villes.
     *
     * @return liste des villes existantes
     */
    @GetMapping
    public List<Ville> getAllVilles() {
        return villeService.extractVilles();
    }

    /**
     * Récupère une ville par son nom.
     *
     * @param nom nom de la ville à rechercher
     * @return ResponseEntity avec la ville si trouvée, sinon 404
     */
    @GetMapping("/{nom}")
    public ResponseEntity<Ville> getVilleParNom(@PathVariable String nom) {
        return villeService.extractVille(nom)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Ajoute une nouvelle ville en base après validation.
     *
     * @param nouvelleVille nouvel objet Ville à ajouter
     * @param bindingResult conteneur des erreurs de validation
     * @return ResponseEntity avec la liste mise à jour des villes si succès, ou avec les erreurs sinon
     */
    @PostMapping
    public ResponseEntity<?> createVille(@Valid @RequestBody Ville nouvelleVille, BindingResult bindingResult) {

        villeValidator.validate(nouvelleVille, bindingResult);

        if (bindingResult.hasErrors()) {
            List<String> erreurs = bindingResult.getAllErrors()
                    .stream()
                    .map(err -> err.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(erreurs);
        }

        List<Ville> villes = villeService.insertVille(nouvelleVille);
        return ResponseEntity.ok(villes);
    }

    /**
     * Modifie une ville existante identifiée par son nom.
     *
     * @param nom           nom de la ville à modifier
     * @param villeModifiee objet Ville contenant les nouvelles données
     * @return ResponseEntity avec la liste mise à jour des villes si succès, ou avec les erreurs sinon
     */
    @PutMapping("/{nom}")
    public ResponseEntity<?> updateVille(@PathVariable String nom, @RequestBody Ville villeModifiee) {

        Errors erreurs = new BeanPropertyBindingResult(villeModifiee, "ville");
        villeValidator.validate(villeModifiee, erreurs);

        if (erreurs.hasErrors()) {
            List<String> messagesErreurs = erreurs.getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();

            return ResponseEntity.badRequest().body(messagesErreurs);
        }

        List<Ville> villes = villeService.modifierVilleParNom(nom, villeModifiee);
        return ResponseEntity.ok(villes);
    }


    /**
     * Supprime une ville par son nom.
     *
     * @param nom nom de la ville à supprimer
     * @return ResponseEntity avec la liste mise à jour des villes après suppression
     */
    @DeleteMapping("/{nom}")
    public ResponseEntity<List<Ville>> deleteVille(@PathVariable String nom) {
        // Appeler méthode service qui supprime ville par nom
        List<Ville> villes = villeService.supprimerVilleParNom(nom);
        return ResponseEntity.ok(villes);
    }
}
