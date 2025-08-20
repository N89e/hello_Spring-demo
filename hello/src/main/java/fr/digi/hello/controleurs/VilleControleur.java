package fr.digi.hello.controleurs;

import fr.digi.hello.services.Ville;
import fr.digi.hello.validators.VilleValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Contrôleur REST pour gérer les opérations CRUD sur les villes.
 * Cette classe expose des endpoints pour récupérer, ajouter et modifier des villes.
 */
@RestController
@RequestMapping("/villes")
public class VilleControleur {

    /**
     * Liste thread-safe stockant les villes en mémoire.
     */
    private List<Ville> villes = new CopyOnWriteArrayList<>();

    /**
     * Constructeur initialisant la liste des villes avec quelques entrées par défaut.
     */
    public VilleControleur() {
        villes.add(new Ville(1, "Paris", 2148000));
        villes.add(new Ville(2, "Lyon", 515695));
        villes.add(new Ville(3, "Marseille", 861635));
    }

    /**
     * Endpoint GET pour récupérer la liste complète des villes.
     *
     * @return la liste de toutes les villes actuellement stockées {@link Ville}.
     */
    @GetMapping
    public List<Ville> getVilles() {
        return villes;
    }

    /**
     * Endpoint GET pour récupérer une ville par son nom.
     *
     * @param nom Le nom de la ville à rechercher (paramètre de requête).
     * @return La ville correspondante si trouvée, sinon une réponse 404 Not Found.
     */
    @GetMapping("/nom")
    public ResponseEntity<Ville> getVilleParNom(@RequestParam String nom) {
        for (Ville v : villes) {
            if (v.getNom().equalsIgnoreCase(nom)) {
                return ResponseEntity.ok(v);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Endpoint POST pour ajouter une nouvelle ville.
     * La validation Bean Validation est appliquée sur l'objet Ville reçu.
     * L'id de la nouvelle ville est auto-incrémenté automatiquement.
     *
     * @param nouvelleVille La ville à ajouter (corps de la requête JSON).
     * @param bindingResult Contient les résultats de la validation.
     * @return Un message de succès ou la liste des erreurs de validation avec un statut 400.
     */
    @PostMapping
    public ResponseEntity<?> ajouterVille(@Valid @RequestBody Ville nouvelleVille, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> erreurs = bindingResult.getAllErrors()
                    .stream()
                    .map(err -> err.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(erreurs);
        }

        boolean existe = villes.stream()
                .anyMatch(v -> v.getNom().equalsIgnoreCase(nouvelleVille.getNom()));

        if (existe) {
            return ResponseEntity.badRequest().body("La ville existe déjà");
        }

        // Auto-incrémenter l'id par sécurité (à supprimer lorsqu'il y aura une base de donnée)
        int maxId = villes.stream()
                .mapToInt(Ville::getId)
                .max()
                .orElse(0);
        nouvelleVille.setId(maxId + 1);

        villes.add(nouvelleVille);
        return ResponseEntity.ok("Ville insérée avec succès");
    }

    @Autowired
    private VilleValidator villeValidator;

    /**
     * Endpoint PUT pour modifier une ville en la recherchant par son nom passé en URL.
     * Valide les données envoyées, recherche la ville existante et met à jour ses attributs.
     * Le champ id de la ville est conservé.
     *
     * @param nom Le nom actuel de la ville à modifier, passé en tant que variable d'URL.
     * @param modifVille L’objet Ville contenant les nouvelles valeurs à appliquer (corps JSON).
     * @return Un message de succès ou les erreurs de validation / non-trouvabilité correspondant.
     */
    @PutMapping("/{nom}")
    public ResponseEntity<?> modifierVilleParNom(
            @PathVariable String nom,
            @RequestBody Ville modifVille) {

        Errors erreurs = new BeanPropertyBindingResult(modifVille, "ville");
        villeValidator.validate(modifVille, erreurs);

        if (erreurs.hasErrors()) {
            List<String> messagesErreurs = erreurs.getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();

            return ResponseEntity.badRequest().body(messagesErreurs);
        }

        // Recherche ville existante par nom dans l'URL
        Ville villeExistante = villes.stream()
                .filter(v -> v.getNom().equalsIgnoreCase(nom))
                .findFirst()
                .orElse(null);

        if (villeExistante == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Ville introuvable avec le nom : " + nom);
        }

        // Conserver l'id
        int idConserve = villeExistante.getId();

        // Mise à jour des champs
        villeExistante.setNom(modifVille.getNom());
        villeExistante.setNbHabitants(modifVille.getNbHabitants());

        // Réaffecter l'id conservé
        villeExistante.setId(idConserve);

        return ResponseEntity.ok("Ville modifiée avec succès");
    }

    /**
     * Supprime une ville existante en la recherchant par son nom.
     *
     * @param nom Le nom de la ville à supprimer, passé en tant que variable d'URL.
     * @return Une réponse HTTP indiquant le succès de la suppression ou une erreur 404 si la ville n'existe pas.
     */
    @DeleteMapping("/{nom}")
    public ResponseEntity<?> supprimerVilleParNom(@PathVariable String nom) {
        // Recherche de la ville existante par nom dans la liste
        Ville villeExistante = villes.stream()
                .filter(v -> v.getNom().equalsIgnoreCase(nom))
                .findFirst()
                .orElse(null);

        if (villeExistante == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Ville introuvable avec le nom : " + nom);
        }

        // Suppression de la ville de la liste
        villes.remove(villeExistante);

        return ResponseEntity.ok("Ville supprimée avec succès");
    }



}
