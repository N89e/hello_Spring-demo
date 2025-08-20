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
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/villes")
public class VilleControleur {

    // Liste thread-safe
    private List<Ville> villes = new CopyOnWriteArrayList<>();

    // Initialisation dans le constructeur
    public VilleControleur() {
        villes.add(new Ville(1, "Paris", 2148000));
        villes.add(new Ville(2, "Lyon", 515695));
        villes.add(new Ville(3, "Marseille", 861635));
    }

    @GetMapping
    public List<Ville> getVilles() {
        return villes;
    }

    @GetMapping("/nom")
    public ResponseEntity<Ville> getVilleParNom(@RequestParam String nom) {
        for (Ville v : villes) {
            if (v.getNom().equalsIgnoreCase(nom)) {
                return ResponseEntity.ok(v);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

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

        // Auto-incrémenter l'id par sécurité (si besoin)
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

        // Recherche ville existante par nom dans l'URL (ancien nom)
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

        // Mettre à jour les champs avec les données reçues
        // Attention, ici modifVille.getNom() peut être différent de 'nom' dans URL
        villeExistante.setNom(modifVille.getNom());
        villeExistante.setNbHabitants(modifVille.getNbHabitants());

        // Réaffecter l'id conservé (optionnel)
        villeExistante.setId(idConserve);

        return ResponseEntity.ok("Ville modifiée avec succès");
    }


}
