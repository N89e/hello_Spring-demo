package fr.digi.hello.controleurs;

import fr.digi.hello.dto.DepartementDto;
import fr.digi.hello.dto.VilleDto;
import fr.digi.hello.mappers.MapperUtil;
import fr.digi.hello.items.Departement;
import fr.digi.hello.services.DepartementService;
import fr.digi.hello.services.ImplVilleService;
import fr.digi.hello.validators.DepartementValidator;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des départements.
 * Permet d'effectuer des opérations CRUD via des endpoints HTTP.
 */
@RestController
@RequestMapping("/departements")
@Validated
public class DepartementControleur {

    private final ImplVilleService implVilleService;
    private final DepartementService departementService;
    private final DepartementValidator departementValidator;

    /**
     * Constructeur avec injection des dépendances nécessaires.
     *
     * @param departementService service métier pour les départements
     * @param departementValidator validateur personnalisé pour les départements
     */
    public DepartementControleur(ImplVilleService implVilleService, DepartementService departementService, DepartementValidator departementValidator) {
        this.implVilleService = implVilleService;
        this.departementService = departementService;
        this.departementValidator = departementValidator;
    }

    /**
     * Récupère la liste de tous les départements.
     *
     * @return liste de DTO des départements
     */
    @GetMapping
    public List<DepartementDto> getAllDepartements() {
        return departementService.extractDepartements().stream()
                .map(MapperUtil::toDepartementDto)
                .toList();
    }

    /**
     * Récupère un département par son identifiant.
     *
     * @param id identifiant du département (doit être positif)
     * @return DTO du département si trouvé, 404 sinon
     */
    @GetMapping("/{id}")
    public ResponseEntity<DepartementDto> getDepartementById(@PathVariable @Positive Integer id) {
        return departementService.extractDepartement(id)
                .map(MapperUtil::toDepartementDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère un département par son nom (insensible à la casse).
     *
     * @param nom nom du département
     * @return DTO du département si trouvé, 404 sinon
     */
    @GetMapping("/nom/{nom}")
    public ResponseEntity<DepartementDto> getDepartementByNom(@PathVariable String nom) {
        return departementService.extractDepartement(nom)
                .map(MapperUtil::toDepartementDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Liste les n plus grandes villes (par habitants) d’un département donné.
     * @param nom nom du département
     * @param n nombre de villes à retourner (par défaut 3)
     * @return liste DTO des villes
     */
    @GetMapping("/departements/{nom}/plus-grandes")
    public ResponseEntity<List<VilleDto>> getNPlusGrandesVilles(
            @PathVariable String nom,
            @RequestParam(defaultValue = "3") int n) {
        List<VilleDto> villes = implVilleService.nVillesByDepartementOrderByPopulationDesc(nom, n)
                .stream().map(MapperUtil::toVilleDto).toList();
        return ResponseEntity.ok(villes);
    }

    @GetMapping("/departements/{nom}/par-population")
    public ResponseEntity<List<VilleDto>> getVillesParPopulation(
            @PathVariable String nom,
            @RequestParam int min,
            @RequestParam int max) {
        List<VilleDto> villes = implVilleService.findVillesByDepartementAndPopulationBetween(nom, min, max)
                .stream().map(MapperUtil::toVilleDto).toList();
        return ResponseEntity.ok(villes);
    }

    /**
     * Crée un nouveau département.
     * Valide le DTO et vérifie l'absence de doublon.
     *
     * @param departementDto DTO du département à créer
     * @param bindingResult résultat de la validation
     * @return liste mise à jour des départements, ou erreurs 400
     */
    @PostMapping
    public ResponseEntity<?> createDepartement(@RequestBody DepartementDto departementDto, BindingResult bindingResult) {
        departementValidator.validate(departementDto, bindingResult);

        // Vérifier doublon code département
        if (departementService.findByCode(departementDto.getCode()).isPresent()) {
            bindingResult.rejectValue("code", "CodeDptExiste", "Un département avec ce code existe déjà");
        }

        if (bindingResult.hasErrors()) {
            List<String> erreurs = bindingResult.getAllErrors()
                    .stream()
                    .map(e -> e.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(erreurs);
        }

        // Vérifier doublon nom département
        if (departementService.extractDepartement(departementDto.getNom()).isPresent()) {
            return ResponseEntity.badRequest().body("Un département avec ce nom existe déjà");
        }

        Departement departement = MapperUtil.toDepartement(departementDto);
        List<Departement> departements = departementService.insertDepartement(departement);
        List<DepartementDto> dtos = departements.stream()
                .map(MapperUtil::toDepartementDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }



    /**
     * Modifie un département existant identifié par son nom.
     * Valide le DTO, vérifie l'existence de l'ancien département et l'absence de doublon avec le nouveau nom.
     *
     * @param nom nom actuel du département à modifier
     * @param departementDto données modifiées du département
     * @param bindingResult résultat de la validation
     * @return liste mise à jour des départements, ou erreurs 400
     */
    @PutMapping("/nom/{nom}")
    public ResponseEntity<?> updateDepartementNom(@PathVariable String nom,
                                                  @RequestBody DepartementDto departementDto,
                                                  BindingResult bindingResult) {
        departementValidator.validate(departementDto, bindingResult);
        if (bindingResult.hasErrors()) {
            List<String> erreurs = bindingResult.getAllErrors().stream()
                    .map(e -> e.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(erreurs);
        }

        if (departementService.extractDepartement(nom).isEmpty()) {
            return ResponseEntity.badRequest().body("Le département à modifier n'existe pas");
        }

        if (!nom.equals(departementDto.getNom()) && departementService.extractDepartement(departementDto.getNom()).isPresent()) {
            return ResponseEntity.badRequest().body("Un département avec ce nom existe déjà");
        }

        Departement departement = MapperUtil.toDepartement(departementDto);
        List<Departement> departements = departementService.modifierDepartementParNom(nom, departement);
        List<DepartementDto> dtos = departements.stream()
                .map(MapperUtil::toDepartementDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Modifie un département existant identifié par son identifiant.
     * Valide le DTO, vérifie l'existence du département et l'absence de doublon.
     *
     * @param id identifiant du département à modifier
     * @param departementDto données modifiées du département
     * @param bindingResult résultat de la validation
     * @return liste mise à jour des départements, ou erreurs HTTP appropriées
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDepartement(@PathVariable @Positive Integer id,
                                               @RequestBody DepartementDto departementDto,
                                               BindingResult bindingResult) {
        departementValidator.validate(departementDto, bindingResult);
        if (bindingResult.hasErrors()) {
            List<String> erreurs = bindingResult.getAllErrors().stream()
                    .map(e -> e.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(erreurs);
        }
        if (departementService.extractDepartement(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (departementService.extractDepartement(departementDto.getNom()).isPresent()) {
            return ResponseEntity.badRequest().body("Un département avec ce nom existe déjà");
        }

        Departement departement = MapperUtil.toDepartement(departementDto);
        List<Departement> departements = departementService.modifierDepartement(id, departement);
        List<DepartementDto> dtos = departements.stream()
                .map(MapperUtil::toDepartementDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Supprime un département identifié par son identifiant.
     * Vérifie que le département existe avant suppression.
     *
     * @param id identifiant du département à supprimer
     * @return liste mise à jour des départements, ou message d'erreur si inexistant
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDepartement(@PathVariable @Positive Integer id) {
        if (departementService.extractDepartement(id).isEmpty()) {
            return ResponseEntity.badRequest().body("Le département à supprimer n'existe pas !");
        }

        List<Departement> departements = departementService.supprimerDepartement(id);
        List<DepartementDto> dtos = departements.stream()
                .map(MapperUtil::toDepartementDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Supprime un département identifié par son nom.
     * Vérifie que le département existe avant suppression.
     *
     * @param nom nom du département à supprimer
     * @return liste mise à jour des départements, ou message d'erreur si inexistant
     */
    @DeleteMapping("/nom/{nom}")
    public ResponseEntity<?> deleteDepartementParNom(@PathVariable String nom) {
        if (departementService.extractDepartement(nom).isEmpty()) {
            return ResponseEntity.badRequest().body("Le département à supprimer n'existe pas !");
        }

        List<Departement> departements = departementService.supprimerDepartementParNom(nom);
        List<DepartementDto> dtos = departements.stream()
                .map(MapperUtil::toDepartementDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }
}
